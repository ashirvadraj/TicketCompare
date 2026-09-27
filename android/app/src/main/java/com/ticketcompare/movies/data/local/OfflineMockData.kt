package com.ticketcompare.movies.data.local

import com.ticketcompare.movies.data.model.*

object OfflineMockData {
    val sampleMovies = listOf(
        Movie(
            id = "movie-avatar",
            title = "Avatar: The Way of Water",
            posterUrl = "https://image.tmdb.org/t/p/w500/t6HIqrRAclMCA60NsSmeqe9RmNV.jpg",
            bannerUrl = "https://image.tmdb.org/t/p/original/8YFL5QQVPy3AgrEQxNYvsgiPEbe.jpg",
            durationMinutes = 192,
            genre = listOf("Sci-Fi", "Action", "Adventure"),
            languages = listOf("Hindi", "English", "Tamil", "Telugu"),
            formats = listOf("2D", "3D", "IMAX 3D", "4DX 3D"),
            rating = 8.9,
            voteCount = 14250,
            certification = "UA",
            synopsis = "Jake Sully lives with his newfound family formed on the extrasolar moon Pandora. Once a familiar threat returns to finish what was previously started, Jake must work with Neytiri and the army of the Na'vi race to protect their home.",
            releaseDate = "2026-10-05",
            cast = listOf("Sam Worthington", "Zoe Saldana", "Sigourney Weaver", "Stephen Lang", "Kate Winslet"),
            director = "James Cameron"
        ),
        Movie(
            id = "movie-kalki",
            title = "Kalki 2898 AD",
            posterUrl = "https://image.tmdb.org/t/p/w500/z0T0q7uM0D99q1aX3x90m4q0p.jpg",
            bannerUrl = "https://image.tmdb.org/t/p/original/w2R3x1lZ2u7A9V7nQ90m4q0p.jpg",
            durationMinutes = 181,
            genre = listOf("Mythology", "Sci-Fi", "Action"),
            languages = listOf("Hindi", "Telugu", "Tamil", "Malayalam"),
            formats = listOf("2D", "3D", "IMAX 3D"),
            rating = 8.7,
            voteCount = 22100,
            certification = "UA",
            synopsis = "A modern avatar of Vishnu, a Hindu god, who is believed to have descended to the earth to protect the world from evil forces.",
            releaseDate = "2026-09-20",
            cast = listOf("Prabhas", "Amitabh Bachchan", "Kamal Haasan", "Deepika Padukone"),
            director = "Nag Ashwin"
        ),
        Movie(
            id = "movie-stree2",
            title = "Stree 2: Sarkate Ka Aatank",
            posterUrl = "https://image.tmdb.org/t/p/w500/4q25XgT7q9vW3m1aX4y8p0q9.jpg",
            bannerUrl = "https://image.tmdb.org/t/p/original/5q36YhU8r0wX4n2bY5z9q1r0.jpg",
            durationMinutes = 147,
            genre = listOf("Comedy", "Horror"),
            languages = listOf("Hindi"),
            formats = listOf("2D"),
            rating = 8.4,
            voteCount = 19800,
            certification = "UA",
            synopsis = "The town of Chanderi is haunted once again by a headless entity. Vicky and his friends band together with Stree.",
            releaseDate = "2026-08-15",
            cast = listOf("Rajkummar Rao", "Shraddha Kapoor", "Pankaj Tripathi"),
            director = "Amar Kaushik"
        )
    )

    val sampleCinemas = listOf(
        Cinema(
            id = "cinema-pvr-moi",
            name = "PVR INOX Mall of India",
            chain = "PVR INOX",
            address = "Sector 18, Noida",
            city = "Noida",
            distanceKm = 2.1,
            supportedPlatforms = listOf("pvr", "bms", "district"),
            facilities = listOf("IMAX Laser", "4DX", "Recliner", "F&B")
        ),
        Cinema(
            id = "cinema-pvr-logix",
            name = "PVR Superplex Logix City Centre",
            chain = "PVR INOX",
            address = "Sector 32, Noida",
            city = "Noida",
            distanceKm = 3.4,
            supportedPlatforms = listOf("pvr", "bms", "district"),
            facilities = listOf("Gold Class", "IMAX", "Dolby Atmos")
        ),
        Cinema(
            id = "cinema-wave-noida",
            name = "Wave Cinemas Noida",
            chain = "Wave Cinemas",
            address = "The Great India Place, Sector 38A, Noida",
            city = "Noida",
            distanceKm = 2.5,
            supportedPlatforms = listOf("bms", "district"),
            facilities = listOf("Platinum Lounge", "Dolby 7.1")
        ),
        Cinema(
            id = "cinema-cinepolis-venice",
            name = "Cinepolis Grand Venice Mall",
            chain = "Cinepolis",
            address = "Greater Noida",
            city = "Noida",
            distanceKm = 8.6,
            supportedPlatforms = listOf("cinepolis", "bms", "district"),
            facilities = listOf("VIP Lounge", "Macro XE")
        )
    )

    fun createSampleShows(movieId: String, cinemaId: String = "cinema-pvr-moi", date: String = "2026-10-05"): List<Show> {
        val times = listOf("10:30 AM", "01:45 PM", "05:15 PM", "08:30 PM")
        return times.map { time ->
            val pvrPrice = ProviderShowPrice(
                platformId = "pvr",
                platformName = "PVR INOX",
                logoUrl = "https://originserver-static1-uat.pvrcinemas.com/newweb/movies/pvr_logo.png",
                ticketPrice = 240,
                convenienceFee = 15,
                internetHandlingFee = 5,
                gstOnFees = 4,
                otherCharges = 0,
                subtotalBeforeDiscounts = 264,
                bestApplicableDiscount = 50,
                bestOfferTitle = "PVR INOX Direct Discount: ₹50 Off",
                finalPayable = 214,
                potentialCashback = 0,
                effectiveCost = 214,
                isAvailable = true,
                seatInventorySupported = true,
                deepLink = "pvr://show/123",
                officialWebCheckout = "https://www.pvrcinemas.com"
            )

            val districtPrice = ProviderShowPrice(
                platformId = "district",
                platformName = "District",
                logoUrl = "https://b.zmtcdn.com/data/edition_assets/district-logo.png",
                ticketPrice = 250,
                convenienceFee = 18,
                internetHandlingFee = 5,
                gstOnFees = 4,
                otherCharges = 0,
                subtotalBeforeDiscounts = 277,
                bestApplicableDiscount = 50,
                bestOfferTitle = "PhonePe UPI ₹50 Off",
                finalPayable = 227,
                potentialCashback = 0,
                effectiveCost = 227,
                isAvailable = true,
                seatInventorySupported = false,
                deepLink = "district://show/123",
                officialWebCheckout = "https://district.in"
            )

            val bmsPrice = ProviderShowPrice(
                platformId = "bms",
                platformName = "BookMyShow",
                logoUrl = "https://assets-in.bmscdn.com/webin/common/icons/logo.svg",
                ticketPrice = 250,
                convenienceFee = 28,
                internetHandlingFee = 5,
                gstOnFees = 6,
                otherCharges = 0,
                subtotalBeforeDiscounts = 289,
                bestApplicableDiscount = 0,
                bestOfferTitle = "GPay ₹75 Cashback (Post-Payment)",
                finalPayable = 289,
                potentialCashback = 75,
                effectiveCost = 214,
                isAvailable = true,
                seatInventorySupported = false,
                deepLink = "bms://show/123",
                officialWebCheckout = "https://in.bookmyshow.com"
            )

            val pricing = listOf(pvrPrice, districtPrice, bmsPrice).sortedBy { it.finalPayable }

            Show(
                id = "show-$cinemaId-${time.replace(":", "").replace(" ", "")}",
                movieId = movieId,
                cinemaId = cinemaId,
                date = date,
                time = time,
                format = "IMAX 3D",
                language = "Hindi",
                screenName = "Audi 03 (Laser)",
                status = if (time == "08:30 PM") "FAST_FILLING" else "AVAILABLE",
                pricing = pricing,
                cheapestPlatformId = pricing.first().platformId,
                cheapestFinalPrice = pricing.first().finalPayable,
                cheapestBasePrice = pricing.first().ticketPrice
            )
        }
    }
}
