package com.ticketcompare.movies.data.api

import com.ticketcompare.movies.data.model.*
import retrofit2.http.*

interface TicketCompareApiService {

    @GET("api/movies")
    suspend fun getMovies(
        @Query("city") city: String,
        @Query("query") query: String? = null
    ): List<Movie>

    @GET("api/movies/{id}")
    suspend fun getMovieDetails(
        @Path("id") movieId: String
    ): Movie

    @GET("api/cinemas")
    suspend fun getCinemas(
        @Query("city") city: String,
        @Query("movieId") movieId: String? = null
    ): List<Cinema>

    @GET("api/shows")
    suspend fun getShows(
        @Query("city") city: String,
        @Query("movieId") movieId: String,
        @Query("cinemaId") cinemaId: String? = null,
        @Query("date") date: String
    ): List<Show>

    @GET("api/shows/{showId}/seat-layout")
    suspend fun getSeatLayout(
        @Path("showId") showId: String,
        @Query("platform") platform: String
    ): SeatLayoutResponse

    @POST("api/pricing/calculate")
    suspend fun calculatePrice(
        @Body request: Map<String, Any?>
    ): PriceBreakdown

    @POST("api/pricing/verify")
    suspend fun verifyPrice(
        @Body request: Map<String, Any?>
    ): PriceVerificationResponse

    @GET("api/offers")
    suspend fun getOffers(
        @Query("category") category: String? = null,
        @Query("bank") bank: String? = null
    ): List<Offer>

    @POST("api/offers/sync")
    suspend fun syncLiveOffers(): LiveOfferSyncResponse

    @POST("api/offers/validate-coupon")
    suspend fun validateCoupon(
        @Body body: Map<String, String>
    ): CouponValidationResponse
}
