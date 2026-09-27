package com.ticketcompare.movies.data.repository

import com.ticketcompare.movies.data.api.ApiClient
import com.ticketcompare.movies.data.local.OfflineMockData
import com.ticketcompare.movies.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MovieRepository {

    private val api = ApiClient.apiService

    suspend fun getMovies(city: String, query: String? = null): List<Movie> = withContext(Dispatchers.IO) {
        try {
            api.getMovies(city, query)
        } catch (e: Exception) {
            val list = OfflineMockData.sampleMovies
            if (query.isNullOrBlank()) list
            else list.filter { it.title.contains(query, ignoreCase = true) }
        }
    }

    suspend fun getMovieDetails(movieId: String): Movie? = withContext(Dispatchers.IO) {
        try {
            api.getMovieDetails(movieId)
        } catch (e: Exception) {
            OfflineMockData.sampleMovies.find { it.id == movieId }
        }
    }

    suspend fun getCinemas(city: String, movieId: String? = null): List<Cinema> = withContext(Dispatchers.IO) {
        try {
            api.getCinemas(city, movieId)
        } catch (e: Exception) {
            OfflineMockData.sampleCinemas
        }
    }

    suspend fun getShows(city: String, movieId: String, cinemaId: String? = null, date: String = OfflineMockData.getDynamicDateStr(0)): List<Show> = withContext(Dispatchers.IO) {
        try {
            api.getShows(city, movieId, cinemaId, date)
        } catch (e: Exception) {
            OfflineMockData.createSampleShows(movieId, cinemaId ?: "cinema-pvr-moi", date)
        }
    }

    suspend fun getSeatLayout(showId: String, platform: String): SeatLayoutResponse = withContext(Dispatchers.IO) {
        try {
            api.getSeatLayout(showId, platform)
        } catch (e: Exception) {
            if (platform.equals("pvr", ignoreCase = true)) {
                SeatLayoutResponse(
                    supported = true,
                    rows = listOf("A", "B", "C", "D", "E"),
                    seats = (1..10).map { num ->
                        SeatItem(
                            id = "D$num",
                            row = "D",
                            number = num,
                            category = "Prime",
                            price = 320,
                            isAvailable = num !in listOf(4, 5)
                        )
                    }
                )
            } else {
                SeatLayoutResponse(
                    supported = false,
                    message = "Seat selection will continue on the provider's official booking page."
                )
            }
        }
    }
}
