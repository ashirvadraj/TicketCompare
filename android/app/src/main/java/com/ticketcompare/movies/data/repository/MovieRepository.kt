package com.ticketcompare.movies.data.repository

import android.util.Log
import com.ticketcompare.movies.data.api.ApiClient
import com.ticketcompare.movies.data.model.*
import com.ticketcompare.movies.data.util.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

/**
 * SHOWTIME-FIRST Movie Repository:
 * Fetches real cinema inventory and valid future showtimes.
 * Maintains short-lived memory cache with strict TTL.
 * NEVER falls back to fake/dummy showtimes or static movie catalogs.
 */
class MovieRepository {

    private val api get() = ApiClient.apiService

    private val _fetchState = MutableStateFlow(ProviderFetchState.IDLE)
    val fetchState: StateFlow<ProviderFetchState> = _fetchState.asStateFlow()

    private val _diagnostics = MutableStateFlow<DiagnosticsSummary?>(null)
    val diagnostics: StateFlow<DiagnosticsSummary?> = _diagnostics.asStateFlow()

    private val _lastErrorMessage = MutableStateFlow<String?>(null)
    val lastErrorMessage: StateFlow<String?> = _lastErrorMessage.asStateFlow()

    // Cache with short TTL (2 minutes for showtimes, 1 hour for cinemas/movie details)
    private var cachedMovies: List<Movie> = emptyList()
    private var moviesCacheCity: String = ""
    private var moviesCacheDate: String = ""
    private var moviesCacheTimestamp: Long = 0L

    private var cachedCinemas: List<Cinema> = emptyList()
    private var cinemasCacheCity: String = ""
    private var cinemasCacheTimestamp: Long = 0L

    private var cachedShows: Map<String, List<Show>> = emptyMap()
    private var showsCacheTimestamp: Long = 0L

    private val SHOWTIME_CACHE_TTL_MS = 120_000L // 2 minutes
    private val METADATA_CACHE_TTL_MS = 3_600_000L // 1 hour

    fun getLastUpdatedSeconds(): Long {
        if (showsCacheTimestamp == 0L && moviesCacheTimestamp == 0L) return 0L
        val latest = maxOf(showsCacheTimestamp, moviesCacheTimestamp)
        return (System.currentTimeMillis() - latest) / 1000
    }

    fun invalidateCache() {
        cachedShows = emptyMap()
        showsCacheTimestamp = 0L
        cachedMovies = emptyList()
        moviesCacheTimestamp = 0L
    }

    suspend fun getMovies(
        city: String,
        date: String? = null,
        query: String? = null,
        forceRefresh: Boolean = false
    ): List<Movie> = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        val activeDate = date ?: DateUtils.getDynamicDateStr(0)

        // Memory cache check
        if (!forceRefresh &&
            city == moviesCacheCity &&
            activeDate == moviesCacheDate &&
            (now - moviesCacheTimestamp) < SHOWTIME_CACHE_TTL_MS &&
            cachedMovies.isNotEmpty()
        ) {
            _fetchState.value = ProviderFetchState.LIVE_ACTIVE
            return@withContext if (query.isNullOrBlank()) cachedMovies
            else cachedMovies.filter { it.title.contains(query, ignoreCase = true) }
        }

        _fetchState.value = ProviderFetchState.LOADING
        Log.d("TicketCompare", ">>> Requesting live movies: City=$city, Date=$activeDate, Timezone=${DateUtils.IST_ZONE_ID.id}")

        try {
            val liveMovies = api.getMovies(city = city, date = activeDate, query = query)
            cachedMovies = liveMovies
            moviesCacheCity = city
            moviesCacheDate = activeDate
            moviesCacheTimestamp = now

            // Also fetch fresh provider diagnostics in parallel
            try {
                val diag = api.getDiagnostics(city = city, date = activeDate)
                _diagnostics.value = diag
                Log.d("TicketCompare", "TicketCompare DEBUG\nCity: $city\nDate: $activeDate\nTimezone: ${diag.timezone}\n" +
                        "Bookable shows: ${diag.bookableShows}\nMovies with bookable shows: ${diag.moviesWithBookableShows}")
            } catch (de: Exception) {
                Log.w("TicketCompare", "Diagnostics fetch skipped: ${de.message}")
            }

            if (liveMovies.isNotEmpty()) {
                _fetchState.value = ProviderFetchState.LIVE_ACTIVE
                _lastErrorMessage.value = null
            } else {
                _fetchState.value = ProviderFetchState.NO_SHOWS_FOUND
                _lastErrorMessage.value = null
            }

            liveMovies
        } catch (e: Exception) {
            Log.e("TicketCompare", "!!! Provider request failed for $city on $activeDate: ${e.message}", e)
            _lastErrorMessage.value = e.localizedMessage ?: "Unable to connect to booking providers."
            _fetchState.value = ProviderFetchState.DATA_UNAVAILABLE

            if ((now - moviesCacheTimestamp) < (SHOWTIME_CACHE_TTL_MS * 3) && cachedMovies.isNotEmpty()) {
                cachedMovies
            } else {
                emptyList()
            }
        }
    }

    suspend fun fetchDiagnostics(city: String, date: String? = null): DiagnosticsSummary? = withContext(Dispatchers.IO) {
        val activeDate = date ?: DateUtils.getDynamicDateStr(0)
        try {
            val diag = api.getDiagnostics(city = city, date = activeDate)
            _diagnostics.value = diag
            diag
        } catch (e: Exception) {
            Log.e("TicketCompare", "Failed to load diagnostics: ${e.message}")
            null
        }
    }

    suspend fun getMovieDetails(movieId: String): Movie? = withContext(Dispatchers.IO) {
        try {
            api.getMovieDetails(movieId)
        } catch (e: Exception) {
            cachedMovies.find { it.id == movieId }
        }
    }

    suspend fun getCinemas(city: String, movieId: String? = null): List<Cinema> = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        if (city == cinemasCacheCity && (now - cinemasCacheTimestamp) < METADATA_CACHE_TTL_MS && cachedCinemas.isNotEmpty()) {
            return@withContext cachedCinemas
        }

        try {
            val liveCinemas = api.getCinemas(city, movieId)
            cachedCinemas = liveCinemas
            cinemasCacheCity = city
            cinemasCacheTimestamp = now
            liveCinemas
        } catch (e: Exception) {
            if (cachedCinemas.isNotEmpty()) cachedCinemas else emptyList()
        }
    }

    suspend fun getShows(
        city: String,
        movieId: String? = null,
        cinemaId: String? = null,
        date: String = DateUtils.getDynamicDateStr(0),
        forceRefresh: Boolean = false
    ): List<Show> = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        val cacheKey = "${city}_${movieId.orEmpty()}_${cinemaId.orEmpty()}_$date"

        if (!forceRefresh && (now - showsCacheTimestamp) < SHOWTIME_CACHE_TTL_MS) {
            val cached = cachedShows[cacheKey]
            if (cached != null) {
                return@withContext cached.filter { show ->
                    show.isBookable && DateUtils.isShowTimeValid(show.date, show.startTimestamp)
                }
            }
        }

        try {
            val liveShows = api.getShows(city = city, movieId = movieId, cinemaId = cinemaId, date = date)
            val validFutureShows = liveShows.filter { show ->
                show.isBookable && DateUtils.isShowTimeValid(show.date, show.startTimestamp)
            }

            val updatedMap = cachedShows.toMutableMap()
            updatedMap[cacheKey] = validFutureShows
            cachedShows = updatedMap
            showsCacheTimestamp = now
            validFutureShows
        } catch (e: Exception) {
            val cached = cachedShows[cacheKey]
            if (cached != null && (now - showsCacheTimestamp) < (SHOWTIME_CACHE_TTL_MS * 2)) {
                cached.filter { show ->
                    show.isBookable && DateUtils.isShowTimeValid(show.date, show.startTimestamp)
                }
            } else {
                emptyList()
            }
        }
    }

    suspend fun verifyShowAvailability(showId: String): ShowVerificationResponse = withContext(Dispatchers.IO) {
        try {
            api.verifyShowAvailability(showId)
        } catch (e: Exception) {
            ShowVerificationResponse(
                isBookable = false,
                message = "Unable to verify live booking availability with provider. Please try again."
            )
        }
    }

    suspend fun getSeatLayout(showId: String, platform: String): SeatLayoutResponse = withContext(Dispatchers.IO) {
        try {
            api.getSeatLayout(showId, platform)
        } catch (e: Exception) {
            SeatLayoutResponse(
                supported = false,
                message = "Seat selection will continue on the provider's official booking page."
            )
        }
    }
}
