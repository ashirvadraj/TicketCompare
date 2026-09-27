package com.ticketcompare.movies.data.repository

import com.ticketcompare.movies.data.local.LocalSecureStorage
import com.ticketcompare.movies.data.model.BookingRecord
import com.ticketcompare.movies.data.model.SavedPaymentMethod
import com.ticketcompare.movies.data.model.WatchlistItem

class UserProfileRepository(private val storage: LocalSecureStorage) {

    fun getCity(): String = storage.getSelectedCity()
    fun setCity(city: String) = storage.setSelectedCity(city)

    fun getPaymentMethods(): List<SavedPaymentMethod> = storage.getPaymentMethods()
    fun savePaymentMethods(methods: List<SavedPaymentMethod>) = storage.savePaymentMethods(methods)

    fun togglePaymentMethod(id: String) {
        val list = storage.getPaymentMethods().map {
            if (it.id == id) it.copy(isSelected = !it.isSelected) else it
        }
        storage.savePaymentMethods(list)
    }

    fun addPaymentMethod(method: SavedPaymentMethod) {
        val list = storage.getPaymentMethods().toMutableList()
        list.add(method)
        storage.savePaymentMethods(list)
    }

    fun removePaymentMethod(id: String) {
        val list = storage.getPaymentMethods().filter { it.id != id }
        storage.savePaymentMethods(list)
    }

    fun getBookings(): List<BookingRecord> = storage.getBookings()
    fun addBooking(record: BookingRecord) = storage.saveBooking(record)

    fun getWatchlist(): List<WatchlistItem> = storage.getWatchlist()
    fun toggleWatchlist(item: WatchlistItem): Boolean = storage.toggleWatchlist(item)
}
