package com.ticketcompare.movies.data.local

import com.ticketcompare.movies.data.model.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object OfflineMockData {

    fun getDynamicDateStr(offsetDays: Int = 0): String {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, offsetDays)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(cal.time)
    }

    val sampleMovies = listOf(
        Movie(
            id = "movie-war2",
            title = "War 2",
            posterUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=600&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1518676590629-3dcbd9c5a5c9?w=1200&auto=format&fit=crop&q=80",
            durationMinutes = 168,
            genre = listOf("Action", "Espionage", "Thriller"),
            languages = listOf("Hindi", "Telugu", "Tamil"),
            formats = listOf("2D", "IMAX 3D", "4DX 3D", "Dolby Cinema"),
            rating = 9.1,
            voteCount = 52400,
            certification = "UA",
            synopsis = "Major Kabir Dhaliwal confronts a ferocious rogue operative across Istanbul, Tokyo, and Ladakh in this explosive high-octane chapter of the YRF Spy Universe.",
            releaseDate = "2026-08-14",
            cast = listOf("Hrithik Roshan", "Jr. NTR", "Kiara Advani", "John Abraham", "Ashutosh Rana"),
            director = "Ayan Mukerji"
        ),
        Movie(
            id = "movie-toxic",
            title = "Toxic: A Fairy Tale for Grown-ups",
            posterUrl = "https://images.unsplash.com/photo-1518676590629-3dcbd9c5a5c9?w=600&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1485846234645-a62644f84728?w=1200&auto=format&fit=crop&q=80",
            durationMinutes = 162,
            genre = listOf("Action", "Crime", "Period Noir"),
            languages = listOf("Kannada", "Hindi", "Telugu", "Tamil", "Malayalam"),
            formats = listOf("2D", "IMAX", "Dolby Cinema"),
            rating = 9.3,
            voteCount = 48900,
            certification = "A",
            synopsis = "In 1950s cartel-controlled Goa, an enigmatic underworld kingpin rewires the international narcotics syndicate under an unrelenting reign of blood and loyalty.",
            releaseDate = "2026-09-18",
            cast = listOf("Yash", "Nayanthara", "Kiara Advani", "Huma Qureshi", "Shruti Haasan"),
            director = "Geetu Mohandas"
        ),
        Movie(
            id = "movie-king",
            title = "King",
            posterUrl = "https://images.unsplash.com/photo-1485846234645-a62644f84728?w=600&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=1200&auto=format&fit=crop&q=80",
            durationMinutes = 154,
            genre = listOf("Action", "Crime", "Thriller"),
            languages = listOf("Hindi", "Tamil", "Telugu"),
            formats = listOf("2D", "IMAX", "4DX"),
            rating = 8.9,
            voteCount = 41200,
            certification = "UA",
            synopsis = "An elite retired assassin is drawn back into the lethal underworld when a sinister syndicate targets his protege across the criminal empires of Europe.",
            releaseDate = "2026-09-04",
            cast = listOf("Shah Rukh Khan", "Suhana Khan", "Abhishek Bachchan", "Abhay Verma"),
            director = "Siddharth Anand"
        ),
        Movie(
            id = "movie-ramayana",
            title = "Ramayana: Part 1",
            posterUrl = "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=600&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1518676590629-3dcbd9c5a5c9?w=1200&auto=format&fit=crop&q=80",
            durationMinutes = 185,
            genre = listOf("Epic", "Mythological", "Fantasy", "Action"),
            languages = listOf("Hindi", "Telugu", "Tamil", "Kannada", "Malayalam", "English"),
            formats = listOf("2D", "3D", "IMAX 3D", "Dolby Atmos"),
            rating = 9.5,
            voteCount = 68000,
            certification = "U",
            synopsis = "The legendary epic reimagined with groundbreaking visual grandeur, following Lord Rama virtue, exile, and the divine cosmic quest against Adharma.",
            releaseDate = "2026-09-25",
            cast = listOf("Ranbir Kapoor", "Sai Pallavi", "Yash", "Sunny Deol", "Arun Govil", "Lara Dutta"),
            director = "Nitesh Tiwari"
        ),
        Movie(
            id = "movie-spirit",
            title = "Spirit",
            posterUrl = "https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1485846234645-a62644f84728?w=1200&auto=format&fit=crop&q=80",
            durationMinutes = 170,
            genre = listOf("Action", "Crime", "Cop Drama"),
            languages = listOf("Telugu", "Hindi", "Tamil", "Kannada", "Malayalam"),
            formats = listOf("2D", "IMAX"),
            rating = 8.8,
            voteCount = 36700,
            certification = "A",
            synopsis = "A fierce, unhinged IPS officer wages an uncompromising, brutal war against an entrenched political mafia syndicate.",
            releaseDate = "2026-08-28",
            cast = listOf("Prabhas", "Trisha Krishnan", "Vivek Oberoi", "Prakash Raj"),
            director = "Sandeep Reddy Vanga"
        ),
        Movie(
            id = "movie-love-war",
            title = "Love & War",
            posterUrl = "https://images.unsplash.com/photo-1440404653325-ab127d49abc1?w=600&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1579783900882-c0d3dad7b119?w=1200&auto=format&fit=crop&q=80",
            durationMinutes = 165,
            genre = listOf("Romance", "War", "Drama"),
            languages = listOf("Hindi", "Telugu", "Tamil"),
            formats = listOf("2D", "Dolby Atmos"),
            rating = 8.7,
            voteCount = 28300,
            certification = "UA",
            synopsis = "An intense romantic saga of two Indian Army officers and an artist entangled in an epic triangle of sacrifice, passion, and battlefield heroism.",
            releaseDate = "2026-09-11",
            cast = listOf("Ranbir Kapoor", "Alia Bhatt", "Vicky Kaushal"),
            director = "Sanjay Leela Bhansali"
        ),
        Movie(
            id = "movie-batman2",
            title = "The Batman: Part II",
            posterUrl = "https://images.unsplash.com/photo-1509198397868-475647b2a1e5?w=600&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=1200&auto=format&fit=crop&q=80",
            durationMinutes = 168,
            genre = listOf("Action", "Crime", "Drama", "Mystery"),
            languages = listOf("English", "Hindi", "Tamil", "Telugu"),
            formats = listOf("2D", "IMAX 2D", "4DX", "Dolby Cinema"),
            rating = 9.0,
            voteCount = 44500,
            certification = "UA",
            synopsis = "Gotham City deep winter descends into chaos as Batman uncovers the Court of Owls conspiracy while confronting the rising empire of the Penguin.",
            releaseDate = "2026-09-18",
            cast = listOf("Robert Pattinson", "Colin Farrell", "Andy Serkis", "Jeffrey Wright", "Barry Keoghan"),
            director = "Matt Reeves"
        ),
        Movie(
            id = "movie-spiderman4",
            title = "Spider-Man 4: Street Level Legacy",
            posterUrl = "https://images.unsplash.com/photo-1635805737707-575885ab0820?w=600&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1485846234645-a62644f84728?w=1200&auto=format&fit=crop&q=80",
            durationMinutes = 142,
            genre = listOf("Action", "Adventure", "Sci-Fi"),
            languages = listOf("English", "Hindi", "Tamil", "Telugu"),
            formats = listOf("2D", "3D", "IMAX 3D", "4DX 3D"),
            rating = 9.1,
            voteCount = 49000,
            certification = "UA",
            synopsis = "Peter Parker navigates his isolated double life as New York friendly neighborhood Spider-Man, joining forces with Daredevil against Kingpin martial law.",
            releaseDate = "2026-09-25",
            cast = listOf("Tom Holland", "Zendaya", "Charlie Cox", "Vincent D Onofrio", "Mark Ruffalo"),
            director = "Destin Daniel Cretton"
        ),
        Movie(
            id = "movie-avatar",
            title = "Avatar: Fire and Ash (Showcase)",
            posterUrl = "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?w=600&auto=format&fit=crop&q=80",
            bannerUrl = "https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=1200&auto=format&fit=crop&q=80",
            durationMinutes = 192,
            genre = listOf("Sci-Fi", "Action", "Adventure"),
            languages = listOf("English", "Hindi", "Tamil", "Telugu"),
            formats = listOf("IMAX 3D", "3D", "4DX 3D", "Dolby Cinema"),
            rating = 9.4,
            voteCount = 61200,
            certification = "UA",
            synopsis = "Jake Sully and Neytiri journey across the volcanic Ash regions of Pandora, meeting the aggressive Ash People clan led by Varang.",
            releaseDate = "2026-09-28",
            cast = listOf("Sam Worthington", "Zoe Saldana", "Sigourney Weaver", "Oona Chaplin"),
            director = "James Cameron"
        )
    )

    val sampleCinemas = listOf(
        Cinema(
            id = "cinema-pvr-moi",
            name = "PVR Superplex DLF Mall of India",
            chain = "PVR INOX",
            address = "Sector 18, Noida, Uttar Pradesh 201301",
            city = "Noida",
            distanceKm = 1.8,
            supportedPlatforms = listOf("pvr", "bms", "district"),
            facilities = listOf("IMAX Laser", "4DX", "Gold Class", "In-Seat F&B")
        ),
        Cinema(
            id = "cinema-pvr-logix",
            name = "PVR INOX Superplex Logix City Centre",
            chain = "PVR INOX",
            address = "Sector 32, Noida, Uttar Pradesh 201301",
            city = "Noida",
            distanceKm = 3.2,
            supportedPlatforms = listOf("pvr", "bms", "district"),
            facilities = listOf("Gold Class", "IMAX", "Dolby Atmos", "Reserved Parking")
        ),
        Cinema(
            id = "cinema-wave-noida",
            name = "Wave Cinemas The Great India Place (TGIP)",
            chain = "Wave Cinemas",
            address = "Sector 38A, Opposite DLF MOI, Noida 201301",
            city = "Noida",
            distanceKm = 2.1,
            supportedPlatforms = listOf("bms", "district"),
            facilities = listOf("Platinum Lounge", "Dolby 7.1", "Food Court Attached")
        ),
        Cinema(
            id = "cinema-cinepolis-venice",
            name = "Cinepolis Grand Venice Mall",
            chain = "Cinepolis",
            address = "Plot No SH3, Site IV, Pari Chowk, Greater Noida 201308",
            city = "Noida",
            distanceKm = 7.9,
            supportedPlatforms = listOf("cinepolis", "bms", "district"),
            facilities = listOf("VIP Lounge", "Macro XE Laser", "Junior Screen")
        ),
        Cinema(
            id = "cinema-moviemax-gulshan",
            name = "MovieMax Laserplex Gulshan One29",
            chain = "MovieMax",
            address = "Sector 129, Noida-Greater Noida Expressway, Noida",
            city = "Noida",
            distanceKm = 6.4,
            supportedPlatforms = listOf("bms", "district"),
            facilities = listOf("RGB Laser Projection", "Dolby Surround", "Recliner Seats")
        )
    )

    fun createSampleShows(movieId: String, cinemaId: String = "cinema-pvr-moi", date: String = getDynamicDateStr(0)): List<Show> {
        val times = listOf("10:30 AM", "01:45 PM", "05:15 PM", "08:30 PM", "10:45 PM")
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
                deepLink = "pvr://book?showId=show-$cinemaId&date=$date",
                officialWebCheckout = "https://www.pvrcinemas.com/movies/$movieId?city=noida&date=$date"
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
                deepLink = "district://movies/$movieId?date=$date",
                officialWebCheckout = "https://district.in/movies/$movieId?city=noida&date=$date"
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
                deepLink = "bms://movie/$movieId?date=$date",
                officialWebCheckout = "https://in.bookmyshow.com/buytickets/$movieId-noida/$cinemaId"
            )

            val pricing = listOf(pvrPrice, districtPrice, bmsPrice).sortedBy { it.finalPayable }

            Show(
                id = "show-$cinemaId-${time.replace(":", "").replace(" ", "")}-$date",
                movieId = movieId,
                cinemaId = cinemaId,
                date = date,
                time = time,
                format = "IMAX 3D",
                language = "Hindi",
                screenName = "Audi 02 (Laser)",
                status = if (time == "08:30 PM" || time == "05:15 PM") "FAST_FILLING" else "AVAILABLE",
                pricing = pricing,
                cheapestPlatformId = pricing.first().platformId,
                cheapestFinalPrice = pricing.first().finalPayable,
                cheapestBasePrice = pricing.first().ticketPrice
            )
        }
    }
}
