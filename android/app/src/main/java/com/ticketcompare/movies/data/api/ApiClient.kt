package com.ticketcompare.movies.data.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {
    // 10.0.2.2 is Android Emulator localhost, 127.0.0.1 for local tests
    var baseUrl: String = "http://10.0.2.2:4000/"
        private set

    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .build()

    private var _apiService: TicketCompareApiService = buildRetrofit(baseUrl)

    val apiService: TicketCompareApiService
        get() = _apiService

    fun setCustomBaseUrl(url: String) {
        val sanitized = if (url.endsWith("/")) url else "$url/"
        baseUrl = sanitized
        _apiService = buildRetrofit(sanitized)
    }

    private fun buildRetrofit(url: String): TicketCompareApiService {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(TicketCompareApiService::class.java)
    }
}
