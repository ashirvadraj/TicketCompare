package com.ticketcompare.movies.data.model

data class ProviderCity(
    val provider: String,
    val displayName: String,
    val providerCityId: String,
    val regionCode: String = "NCR"
)

object ProviderCityMapping {
    private val mappings = mapOf(
        "noida" to listOf(
            ProviderCity(provider = "bms", displayName = "Noida", providerCityId = "NOIDA", regionCode = "NCR"),
            ProviderCity(provider = "district", displayName = "Noida", providerCityId = "noida", regionCode = "NCR"),
            ProviderCity(provider = "pvr", displayName = "Noida", providerCityId = "101", regionCode = "NCR"),
            ProviderCity(provider = "cinepolis", displayName = "Noida", providerCityId = "noida", regionCode = "NCR")
        ),
        "delhi" to listOf(
            ProviderCity(provider = "bms", displayName = "Delhi", providerCityId = "NCR", regionCode = "NCR"),
            ProviderCity(provider = "district", displayName = "Delhi", providerCityId = "delhi", regionCode = "NCR"),
            ProviderCity(provider = "pvr", displayName = "Delhi", providerCityId = "102", regionCode = "NCR"),
            ProviderCity(provider = "cinepolis", displayName = "Delhi", providerCityId = "delhi", regionCode = "NCR")
        ),
        "mumbai" to listOf(
            ProviderCity(provider = "bms", displayName = "Mumbai", providerCityId = "MUMBAI", regionCode = "MUM"),
            ProviderCity(provider = "district", displayName = "Mumbai", providerCityId = "mumbai", regionCode = "MUM"),
            ProviderCity(provider = "pvr", displayName = "Mumbai", providerCityId = "201", regionCode = "MUM"),
            ProviderCity(provider = "cinepolis", displayName = "Mumbai", providerCityId = "mumbai", regionCode = "MUM")
        ),
        "bengaluru" to listOf(
            ProviderCity(provider = "bms", displayName = "Bengaluru", providerCityId = "BANG", regionCode = "BLR"),
            ProviderCity(provider = "district", displayName = "Bengaluru", providerCityId = "bangalore", regionCode = "BLR"),
            ProviderCity(provider = "pvr", displayName = "Bengaluru", providerCityId = "301", regionCode = "BLR"),
            ProviderCity(provider = "cinepolis", displayName = "Bengaluru", providerCityId = "bangalore", regionCode = "BLR")
        )
    )

    fun resolveCityId(city: String, providerId: String): String {
        val key = city.trim().lowercase()
        val list = mappings[key] ?: mappings["noida"] ?: emptyList()
        return list.find { it.provider.equals(providerId, ignoreCase = true) }?.providerCityId ?: key
    }
}
