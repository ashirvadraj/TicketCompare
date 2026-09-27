package com.ticketcompare.movies.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Movie(
    val id: String,
    val title: String,
    val posterUrl: String,
    val bannerUrl: String,
    val durationMinutes: Int,
    val genre: List<String>,
    val languages: List<String>,
    val formats: List<String>,
    val rating: Double,
    val voteCount: Int,
    val certification: String,
    val synopsis: String,
    val releaseDate: String,
    val cast: List<String>,
    val director: String
)

@JsonClass(generateAdapter = true)
data class Cinema(
    val id: String,
    val name: String,
    val chain: String,
    val address: String,
    val city: String,
    val distanceKm: Double,
    val supportedPlatforms: List<String>,
    val facilities: List<String>
)

@JsonClass(generateAdapter = true)
data class ProviderShowPrice(
    val platformId: String,
    val platformName: String,
    val logoUrl: String,
    val ticketPrice: Int,
    val convenienceFee: Int,
    val internetHandlingFee: Int,
    val gstOnFees: Int,
    val otherCharges: Int = 0,
    val subtotalBeforeDiscounts: Int,
    val bestApplicableDiscount: Int,
    val bestOfferId: String? = null,
    val bestOfferTitle: String? = null,
    val finalPayable: Int,
    val potentialCashback: Int = 0,
    val effectiveCost: Int,
    val isAvailable: Boolean = true,
    val seatInventorySupported: Boolean = false,
    val deepLink: String,
    val officialWebCheckout: String,
    val isSponsored: Boolean = false
)

@JsonClass(generateAdapter = true)
data class Show(
    val id: String,
    val movieId: String,
    val cinemaId: String,
    val date: String,
    val time: String,
    val format: String,
    val language: String,
    val screenName: String,
    val status: String,
    val isBookable: Boolean = true,
    val availableSeats: Int = 0,
    val totalSeats: Int = 0,
    val startTimestamp: Long? = null,
    val verifiedAtTimestamp: Long? = null,
    val sourceProvider: String? = null,
    val fetchedAt: String? = null,
    val providerShowId: String? = null,
    val pricing: List<ProviderShowPrice>,
    val cheapestPlatformId: String,
    val cheapestFinalPrice: Int,
    val cheapestBasePrice: Int
)

@JsonClass(generateAdapter = true)
data class ShowVerificationResponse(
    val isBookable: Boolean,
    val message: String,
    val show: Show? = null
)

@JsonClass(generateAdapter = true)
data class PriceBreakdown(
    val platformId: String,
    val platformName: String,
    val ticketCount: Int,
    val seatCategory: String = "Classic",
    val basePrice: Int,
    val convenienceFee: Int,
    val internetHandlingFee: Int,
    val gstOnFees: Int,
    val mandatoryCharges: Int = 0,
    val grossAmount: Int,
    val appliedCouponCode: String? = null,
    val couponDiscount: Int = 0,
    val bankDiscount: Int = 0,
    val upiDiscount: Int = 0,
    val walletDiscount: Int = 0,
    val membershipDiscount: Int = 0,
    val totalDiscount: Int = 0,
    val finalPayableAmount: Int,
    val potentialCashback: Int = 0,
    val effectiveCost: Int,
    val appliedOfferIds: List<String> = emptyList(),
    val appliedOfferDescriptions: List<String> = emptyList(),
    val isVerified: Boolean = true,
    val verifiedTimestamp: Long = 0L,
    val verificationAgeSeconds: Int = 10,
    val priceChangeNotice: String? = null
)

@JsonClass(generateAdapter = true)
data class Offer(
    val id: String,
    val category: String, // CREDIT_CARD, DEBIT_CARD, UPI, WALLET, COUPON, MEMBERSHIP
    val title: String,
    val description: String,
    val bank: String? = null,
    val cardNetwork: String? = null,
    val cardType: String? = null,
    val cardTiers: List<String>? = null,
    val providerApp: String? = null,
    val membershipProgram: String? = null,
    val minTransaction: Int,
    val maxDiscount: Int,
    val discountPercentage: Int? = null,
    val flatDiscount: Int? = null,
    val isCashback: Boolean = false,
    val cashbackAmount: Int? = null,
    val cashbackConditions: String? = null,
    val startDate: String,
    val expiryDate: String,
    val applicablePlatforms: List<String>,
    val applicableCinemas: List<String>,
    val applicableMovies: List<String>,
    val applicableDays: List<String>,
    val couponCodeRequired: Boolean = false,
    val couponCode: String? = null,
    val terms: List<String>,
    val source: String,
    val lastVerifiedTimestamp: Long,
    val verificationStatus: String
)

@JsonClass(generateAdapter = true)
data class SavedPaymentMethod(
    val id: String,
    val category: String, // CREDIT_CARD, DEBIT_CARD, UPI, WALLET, MEMBERSHIP
    val bank: String? = null,
    val cardType: String? = null, // CREDIT or DEBIT
    val cardNetwork: String? = null, // VISA, MASTERCARD, RUPAY, AMEX
    val cardTier: String? = null, // e.g. Millennia, Regalia, Coral
    val providerApp: String? = null, // GOOGLE_PAY, PHONEPE, PAYTM, AMAZON_PAY
    val isSelected: Boolean = true
)

@JsonClass(generateAdapter = true)
data class BookingRecord(
    val id: String,
    val movieId: String,
    val movieTitle: String,
    val cinemaName: String,
    val date: String,
    val time: String,
    val seats: List<String>,
    val totalAmountPaid: Int,
    val savingsAmount: Int,
    val cashbackReceived: Int = 0,
    val platformName: String,
    val platformId: String,
    val bookingReference: String,
    val timestamp: Long
)

@JsonClass(generateAdapter = true)
data class WatchlistItem(
    val movieId: String,
    val movieTitle: String,
    val posterUrl: String,
    val targetPrice: Int? = null,
    val preferredCinema: String? = null,
    val addedTimestamp: Long
)

@JsonClass(generateAdapter = true)
data class SeatItem(
    val id: String,
    val row: String,
    val number: Int,
    val category: String,
    val price: Int,
    val isAvailable: Boolean,
    val isSelected: Boolean = false
)

@JsonClass(generateAdapter = true)
data class SeatLayoutResponse(
    val supported: Boolean,
    val rows: List<String>? = null,
    val seats: List<SeatItem>? = null,
    val message: String? = null
)

@JsonClass(generateAdapter = true)
data class PriceVerificationResponse(
    val isVerified: Boolean,
    val verifiedAtTimestamp: Long,
    val verificationAgeSeconds: Int,
    val hasPriceChanged: Boolean,
    val previousPrice: Int?,
    val currentPrice: Int,
    val changeNotice: String?,
    val breakdown: PriceBreakdown
)

@JsonClass(generateAdapter = true)
data class CouponValidationResponse(
    val valid: Boolean,
    val message: String,
    val coupon: Offer? = null
)

@JsonClass(generateAdapter = true)
data class LiveOfferSyncResponse(
    val success: Boolean = true,
    val message: String = "",
    val count: Int = 0,
    val syncedAt: Long = 0L,
    val offers: List<Offer> = emptyList()
)

@JsonClass(generateAdapter = true)
data class ProviderDiagnosticItem(
    val id: String,
    val name: String,
    val isConnected: Boolean,
    val httpStatus: Int,
    val responseTimeMs: Long = 0L,
    val cinemasCount: Int = 0,
    val showsCount: Int = 0,
    val validShowsCount: Int = 0,
    val errorMessage: String? = null,
    val lastSuccessfulFetch: String? = null,
    val requiresCredentials: Boolean = false
)

@JsonClass(generateAdapter = true)
data class DiagnosticsSummary(
    val city: String = "Noida",
    val date: String = "",
    val timezone: String = "Asia/Kolkata",
    val providers: List<ProviderDiagnosticItem> = emptyList(),
    val totalProviders: Int = 0,
    val successfulProviders: Int = 0,
    val totalShows: Int = 0,
    val bookableShows: Int = 0,
    val moviesWithBookableShows: Int = 0,
    val isLiveDataConnected: Boolean = false,
    val message: String? = null
)

enum class ProviderFetchState {
    IDLE,
    LOADING,
    LIVE_ACTIVE,       // At least one provider returned fresh shows
    NO_SHOWS_FOUND,    // APIs worked correctly but there genuinely are 0 shows
    DATA_UNAVAILABLE   // All providers failed or network unreachable
}

