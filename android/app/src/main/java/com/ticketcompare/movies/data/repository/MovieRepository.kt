package com.ticketcompare.movies.data.repository

import com.ticketcompare.movies.data.api.ApiClient
import com.ticketcompare.movies.data.model.*
import com.ticketcompare.movies.data.util.DateUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * SHOWTIME-FIRST Movie Repository:
 * Fetches real cinema inventory and valid future showtimes.
 * Maintains short-lived memory cache with strict TTL.
 * NEVER falls back to fake/dummy showtimes or static movie catalogs.
 */
class MovieRepository {

    private val api = ApiClient.apiService

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
        if (showsCacheTimestamp == 0L) return 0L
        return (System.currentTimeMillis() - showsCacheTimestamp) / 1000
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

        if (!forceRefresh &&
            city == moviesCacheCity &&
            activeDate == moviesCacheDate &&
            (now - moviesCacheTimestamp) < SHOWTIME_CACHE_TTL_MS &&
            cachedMovies.isNotEmpty()
        ) {
            return@withContext if (query.isNullOrBlank()) cachedMovies
            else cachedMovies.filter { it.title.contains(query, ignoreCase = true) }
        }

        try {
            val liveMovies = api.getMovies(city = city, date = activeDate, query = query)
            cachedMovies = liveMovies
            moviesCacheCity = city
            moviesCacheDate = activeDate
            moviesCacheTimestamp = now
            liveMovies
        } catch (e: Exception) {
            if ((now - moviesCacheTimestamp) < (SHOWTIME_CACHE_TTL_MS * 3) && cachedMovies.isNotEmpty()) {
                cachedMovies
            } else {
                emptyList()
            }
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
                    show.isBookable && (show.startTimestamp == null || show.startTimestamp > now)
                }
            }
        }

        try {
            val liveShows = api.getShows(city = city, movieId = movieId, cinemaId = cinemaId, date = date)
            val validFutureShows = liveShows.filter { show ->
                show.isBookable && (show.startTimestamp == null || show.startTimestamp > now)
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
                    show.isBookable && (show.startTimestamp == null || show.startTimestamp > now)
                }
            } else {
                emptyList()
            }
        }
    }

    /**
     * FINAL PRE-BOOKING RECHECK:
     * Validates directly with backend/provider that the selected show
     * is still active, in the future, and has bookable seat inventory.
     */
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
