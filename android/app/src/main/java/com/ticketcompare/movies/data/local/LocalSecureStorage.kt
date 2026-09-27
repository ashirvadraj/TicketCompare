package com.ticketcompare.movies.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.ticketcompare.movies.data.model.SavedPaymentMethod
import com.ticketcompare.movies.data.model.BookingRecord
import com.ticketcompare.movies.data.model.WatchlistItem

class LocalSecureStorage(context: Context) {

    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val paymentMethodListType = Types.newParameterizedType(List::class.java, SavedPaymentMethod::class.java)
    private val paymentAdapter = moshi.adapter<List<SavedPaymentMethod>>(paymentMethodListType)

    private val bookingListType = Types.newParameterizedType(List::class.java, BookingRecord::class.java)
    private val bookingAdapter = moshi.adapter<List<BookingRecord>>(bookingListType)

    private val watchlistType = Types.newParameterizedType(List::class.java, WatchlistItem::class.java)
    private val watchlistAdapter = moshi.adapter<List<WatchlistItem>>(watchlistType)

    private val prefs: SharedPreferences = try {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            "ticketcompare_secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    } catch (e: Exception) {
        // Fallback for Robolectric or legacy emulators
        context.getSharedPreferences("ticketcompare_fallback_prefs", Context.MODE_PRIVATE)
    }

    // --- Saved Payment Methods (NON-SENSITIVE METADATA ONLY: Bank, Tier, Network) ---
    fun savePaymentMethods(methods: List<SavedPaymentMethod>) {
        val json = paymentAdapter.toJson(methods)
        prefs.edit().putString("KEY_SAVED_PAYMENTS", json).apply()
    }

    fun getPaymentMethods(): List<SavedPaymentMethod> {
        val json = prefs.getString("KEY_SAVED_PAYMENTS", null)
        return if (!json.isNullOrEmpty()) {
            try {
                paymentAdapter.fromJson(json) ?: getDefaultPaymentMethods()
            } catch (e: Exception) {
                getDefaultPaymentMethods()
            }
        } else {
            getDefaultPaymentMethods()
        }
    }

    private fun getDefaultPaymentMethods(): List<SavedPaymentMethod> {
        return listOf(
            SavedPaymentMethod(
                id = "pm-hdfc-cc",
                category = "CREDIT_CARD",
                bank = "HDFC",
                cardType = "CREDIT",
                cardNetwork = "VISA",
                cardTier = "Millennia",
                isSelected = true
            ),
            SavedPaymentMethod(
                id = "pm-gpay",
                category = "UPI",
                providerApp = "GOOGLE_PAY",
                isSelected = true
            ),
            SavedPaymentMethod(
                id = "pm-phonepe",
                category = "UPI",
                providerApp = "PHONEPE",
                isSelected = true
            )
        )
    }

    // --- Booking History ---
    fun saveBooking(booking: BookingRecord) {
        val current = getBookings().toMutableList()
        current.add(0, booking)
        val json = bookingAdapter.toJson(current)
        prefs.edit().putString("KEY_BOOKINGS", json).apply()
    }

    fun getBookings(): List<BookingRecord> {
        val json = prefs.getString("KEY_BOOKINGS", null)
        return if (!json.isNullOrEmpty()) {
            try {
                bookingAdapter.fromJson(json) ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            // Seed a sample past booking to showcase analytics immediately
            listOf(
                BookingRecord(
                    id = "BK-10829",
                    movieId = "movie-war2",
                    movieTitle = "War 2",
                    cinemaName = "PVR INOX Mall of India",
                    date = "2026-09-24",
                    time = "05:15 PM",
                    seats = listOf("E7", "E8"),
                    totalAmountPaid = 480,
                    savingsAmount = 150,
                    cashbackReceived = 75,
                    platformName = "PVR INOX",
                    platformId = "pvr",
                    bookingReference = "PVR-MOI-99201",
                    timestamp = System.currentTimeMillis() - 4 * 86400000L
                )
            )
        }
    }

    // --- Watchlist ---
    fun getWatchlist(): List<WatchlistItem> {
        val json = prefs.getString("KEY_WATCHLIST", null)
        return if (!json.isNullOrEmpty()) {
            try {
                watchlistAdapter.fromJson(json) ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        } else {
            listOf(
                WatchlistItem(
                    movieId = "movie-toxic",
                    movieTitle = "Toxic: A Fairy Tale for Grown-ups",
                    posterUrl = "https://images.unsplash.com/photo-1518676590629-3dcbd9c5a5c9?w=600&auto=format&fit=crop&q=80",
                    targetPrice = 250,
                    preferredCinema = "PVR INOX Mall of India",
                    addedTimestamp = System.currentTimeMillis()
                )
            )
        }
    }

    fun toggleWatchlist(item: WatchlistItem): Boolean {
        val list = getWatchlist().toMutableList()
        val index = list.indexOfFirst { it.movieId == item.movieId }
        val added: Boolean
        if (index >= 0) {
            list.removeAt(index)
            added = false
        } else {
            list.add(item)
            added = true
        }
        prefs.edit().putString("KEY_WATCHLIST", watchlistAdapter.toJson(list)).apply()
        return added
    }

    // --- Selected City Preference ---
    fun getSelectedCity(): String = prefs.getString("KEY_CITY", "Noida") ?: "Noida"
    fun setSelectedCity(city: String) = prefs.edit().putString("KEY_CITY", city).apply()
}
