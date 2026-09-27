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
            id = "movie-devara",
            title = "Devara: Part 1",
            posterUrl = "https://image.tmdb.org/t/p/w500/A1gC20tU51g5u9o7n8b6c4e2y9q.jpg",
            bannerUrl = "https://image.tmdb.org/t/p/original/8YFL5QQVPy3AgrEQxNYvsgiPEbe.jpg",
            durationMinutes = 178,
            genre = listOf("Action", "Drama", "Thriller"),
            languages = listOf("Hindi", "Telugu", "Tamil", "Kannada", "Malayalam"),
            formats = listOf("2D", "IMAX 3D", "4DX 3D", "Dolby Cinema"),
            rating = 8.8,
            voteCount = 38500,
            certification = "UA",
            synopsis = "An epic coastal action thriller chronicling fear, honor, and redemption across turbulent tides as a fearless warrior protects his people.",
            releaseDate = "2026-09-27",
            cast = listOf("NTR Jr.", "Janhvi Kapoor", "Saif Ali Khan", "Prakash Raj", "Srikanth"),
            director = "Koratala Siva"
        ),
        Movie(
            id = "movie-stree2",
            title = "Stree 2: Sarkate Ka Aatank",
            posterUrl = "https://image.tmdb.org/t/p/w780/nfnhwfUEFuSOxxf4jDdBlY6Lccw.jpg",
            bannerUrl = "https://image.tmdb.org/t/p/original/5q36YhU8r0wX4n2bY5z9q1r0.jpg",
            durationMinutes = 147,
            genre = listOf("Comedy", "Horror"),
            languages = listOf("Hindi"),
            formats = listOf("2D", "4DX"),
            rating = 8.6,
            voteCount = 42100,
            certification = "UA",
            synopsis = "The town of Chanderi faces a terrifying new headless entity, Sarkata. Vicky and his loyal friends team up with Stree to save the women of Chanderi in this blockbuster sequel.",
            releaseDate = "2026-08-15",
            cast = listOf("Rajkummar Rao", "Shraddha Kapoor", "Pankaj Tripathi", "Abhishek Banerjee", "Aparshakti Khurana"),
            director = "Amar Kaushik"
        ),
        Movie(
            id = "movie-tumbbad",
            title = "Tumbbad (Re-release)",
            posterUrl = "https://image.tmdb.org/t/p/w500/7aZ8fT6N1fW6o8oWv6Y9a0b1c2d.jpg",
            bannerUrl = "https://image.tmdb.org/t/p/original/rLb2cwF3Pazuxaj0sRXQ037tGI1.jpg",
            durationMinutes = 104,
            genre = listOf("Horror", "Fantasy", "Period Drama"),
            languages = listOf("Hindi"),
            formats = listOf("2D", "IMAX"),
            rating = 8.9,
            voteCount = 29400,
            certification = "A",
            synopsis = "A mythological horror masterpiece exploring the destructive nature of human greed centered around the cursed goddess of prosperity, Hastar.",
            releaseDate = "2026-09-13",
            cast = listOf("Sohum Shah", "Jyoti Malshe", "Anita Date", "Ronjini Chakraborty"),
            director = "Rahi Anil Barve"
        ),
        Movie(
            id = "movie-buckingham",
            title = "The Buckingham Murders",
            posterUrl = "https://image.tmdb.org/t/p/w500/8q25XgT7q9vW3m1aX4y8p0q9a1b.jpg",
            bannerUrl = "https://image.tmdb.org/t/p/original/w2R3x1lZ2u7A9V7nQ90m4q0p.jpg",
            durationMinutes = 110,
            genre = listOf("Crime", "Mystery", "Thriller"),
            languages = listOf("Hindi", "English"),
            formats = listOf("2D"),
            rating = 8.1,
            voteCount = 11200,
            certification = "UA",
            synopsis = "A grieving detective investigates the murder of a ten-year-old boy in Buckinghamshire while confronting deep community prejudices and inner trauma.",
            releaseDate = "2026-09-13",
            cast = listOf("Kareena Kapoor Khan", "Ash Tandon", "Keith Allen", "Ranveer Brar"),
            director = "Hansal Mehta"
        ),
        Movie(
            id = "movie-transformers",
            title = "Transformers One",
            posterUrl = "https://image.tmdb.org/t/p/w500/iRCgqpdVE4wyLQvKdU01w2oQjN3.jpg",
            bannerUrl = "https://image.tmdb.org/t/p/original/7s23ZhU9s1xY5o3cZ6a0r2s1.jpg",
            durationMinutes = 104,
            genre = listOf("Animation", "Action", "Sci-Fi"),
            languages = listOf("English", "Hindi"),
            formats = listOf("2D", "3D", "IMAX 3D", "4DX 3D"),
            rating = 8.5,
            voteCount = 18700,
            certification = "UA",
            synopsis = "The untold origin story of how legendary brothers-in-arms Orion Pax and D-16 transformed into sworn enemies: Optimus Prime and Megatron.",
            releaseDate = "2026-09-20",
            cast = listOf("Chris Hemsworth", "Brian Tyree Henry", "Scarlett Johansson", "Keegan-Michael Key"),
            director = "Josh Cooley"
        ),
        Movie(
            id = "movie-goat",
            title = "The Greatest of All Time (GOAT)",
            posterUrl = "https://image.tmdb.org/t/p/w500/9yZ6a0r2s1xY5o3cZ7s23ZhU9s1.jpg",
            bannerUrl = "https://image.tmdb.org/t/p/original/8Gxv8gSFCU0XGDykEGv7zR1n2ua.jpg",
            durationMinutes = 179,
            genre = listOf("Action", "Sci-Fi", "Thriller"),
            languages = listOf("Tamil", "Hindi", "Telugu"),
            formats = listOf("2D", "IMAX"),
            rating = 8.3,
            voteCount = 31000,
            certification = "UA",
            synopsis = "A special anti-terrorist squad veteran is haunted by unresolved consequences from a past mission, forcing an explosive confrontation across decades.",
            releaseDate = "2026-09-05",
            cast = listOf("Thalapathy Vijay", "Prashanth", "Prabhu Deva", "Mohan", "Sneha"),
            director = "Venkat Prabhu"
        ),
        Movie(
            id = "movie-yudhra",
            title = "Yudhra",
            posterUrl = "https://image.tmdb.org/t/p/w500/5q36YhU8r0wX4n2bY5z9q1r0a2b.jpg",
            bannerUrl = "https://image.tmdb.org/t/p/original/5q36YhU8r0wX4n2bY5z9q1r0.jpg",
            durationMinutes = 142,
            genre = listOf("Action", "Thriller"),
            languages = listOf("Hindi"),
            formats = listOf("2D"),
            rating = 7.9,
            voteCount = 9800,
            certification = "A",
            synopsis = "A young man with extreme anger management issues goes undercover into an international syndicate to uncover the truth behind his parents death.",
            releaseDate = "2026-09-20",
            cast = listOf("Siddhant Chaturvedi", "Malavika Mohanan", "Raghav Juyal", "Gajraj Rao"),
            director = "Ravi Udyawar"
        ),
        Movie(
            id = "movie-jigra",
            title = "Jigra (Advance Booking)",
            posterUrl = "https://image.tmdb.org/t/p/w500/4q25XgT7q9vW3m1aX4y8p0q9b3c.jpg",
            bannerUrl = "https://image.tmdb.org/t/p/original/w2R3x1lZ2u7A9V7nQ90m4q0p.jpg",
            durationMinutes = 153,
            genre = listOf("Action", "Drama"),
            languages = listOf("Hindi", "Telugu"),
            formats = listOf("2D", "IMAX"),
            rating = 9.0,
            voteCount = 15400,
            certification = "UA",
            synopsis = "A fiercely protective sister undertakes an impossible high-stakes rescue mission across hostile territory to save her imprisoned younger brother.",
            releaseDate = "2026-10-11",
            cast = listOf("Alia Bhatt", "Vedang Raina", "Manoj Pahwa", "Rahul Ravindran"),
            director = "Vasan Bala"
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
