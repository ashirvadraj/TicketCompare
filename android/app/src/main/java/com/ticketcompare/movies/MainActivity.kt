package com.ticketcompare.movies

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ticketcompare.movies.data.local.LocalSecureStorage
import com.ticketcompare.movies.data.model.Cinema
import com.ticketcompare.movies.data.model.Movie
import com.ticketcompare.movies.data.model.Offer
import com.ticketcompare.movies.data.model.SavedPaymentMethod
import com.ticketcompare.movies.data.model.Show
import com.ticketcompare.movies.data.model.WatchlistItem
import com.ticketcompare.movies.data.repository.MovieRepository
import com.ticketcompare.movies.data.repository.OfferRepository
import com.ticketcompare.movies.data.repository.UserProfileRepository
import com.ticketcompare.movies.ui.screens.*
import com.ticketcompare.movies.ui.theme.CinemaGold
import com.ticketcompare.movies.ui.theme.ElectricIndigo
import com.ticketcompare.movies.ui.theme.TicketCompareTheme
import kotlinx.coroutines.launch

enum class Screen {
    HOME, SEARCH, MOVIE_DETAIL, PRICE_CALCULATOR, SEAT_SELECTION, OFFERS, BOOKINGS, PROFILE, WATCHLIST
}

data class NavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector
)

class MainActivity : ComponentActivity() {

    private val movieRepo = MovieRepository()
    private val offerRepo = OfferRepository()
    private lateinit var storage: LocalSecureStorage
    private lateinit var userProfileRepo: UserProfileRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        storage = LocalSecureStorage(applicationContext)
        userProfileRepo = UserProfileRepository(storage)

        setContent {
            TicketCompareTheme {
                TicketCompareMainApp(
                    movieRepo = movieRepo,
                    offerRepo = offerRepo,
                    userProfileRepo = userProfileRepo
                )
            }
        }
    }
}

@Composable
fun TicketCompareMainApp(
    movieRepo: MovieRepository,
    offerRepo: OfferRepository,
    userProfileRepo: UserProfileRepository
) {
    val coroutineScope = rememberCoroutineScope()

    var currentScreen by remember { mutableStateOf(Screen.HOME) }
    var currentCity by remember { mutableStateOf(userProfileRepo.getCity()) }
    var movies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var cinemas by remember { mutableStateOf<List<Cinema>>(emptyList()) }
    var offers by remember { mutableStateOf<List<Offer>>(emptyList()) }
    var paymentMethods by remember { mutableStateOf<List<SavedPaymentMethod>>(emptyList()) }
    var watchlist by remember { mutableStateOf<List<WatchlistItem>>(emptyList()) }

    var selectedMovie by remember { mutableStateOf<Movie?>(null) }
    var selectedShow by remember { mutableStateOf<Show?>(null) }
    var selectedPlatformForSeats by remember { mutableStateOf("pvr") }
    var showsForSelectedMovie by remember { mutableStateOf<List<Show>>(emptyList()) }
    var isSyncingOffers by remember { mutableStateOf(false) }
    var lastOffersSyncTime by remember { mutableStateOf(System.currentTimeMillis()) }

    // Initial Data Fetch
    LaunchedEffect(currentCity) {
        coroutineScope.launch {
            movies = movieRepo.getMovies(currentCity)
            cinemas = movieRepo.getCinemas(currentCity)
            offers = offerRepo.getOffers()
            paymentMethods = userProfileRepo.getPaymentMethods()
            watchlist = userProfileRepo.getWatchlist()
            if (selectedMovie == null && movies.isNotEmpty()) {
                selectedMovie = movies.first()
            }
        }
    }

    val navItems = listOf(
        NavItem(Screen.HOME, "Home", Icons.Default.Home),
        NavItem(Screen.SEARCH, "Search", Icons.Default.Search),
        NavItem(Screen.OFFERS, "Offers", Icons.Default.LocalOffer),
        NavItem(Screen.BOOKINGS, "Bookings", Icons.Default.ConfirmationNumber),
        NavItem(Screen.PROFILE, "Profile", Icons.Default.AccountCircle)
    )

    val showBottomBar = currentScreen in listOf(Screen.HOME, Screen.SEARCH, Screen.OFFERS, Screen.BOOKINGS, Screen.PROFILE)

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp
                ) {
                    navItems.forEach { item ->
                        val isSelected = currentScreen == item.screen
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { currentScreen = item.screen },
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label,
                                    tint = if (isSelected) CinemaGold else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            },
                            label = {
                                Text(
                                    text = item.label,
                                    fontSize = 11.sp,
                                    color = if (isSelected) CinemaGold else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = ElectricIndigo.copy(alpha = 0.2f)
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                Screen.HOME -> {
                    HomeScreen(
                        currentCity = currentCity,
                        onCityChanged = {
                            currentCity = it
                            userProfileRepo.setCity(it)
                        },
                        movies = movies,
                        cinemas = cinemas,
                        onMovieClick = { movie ->
                            selectedMovie = movie
                            coroutineScope.launch {
                                showsForSelectedMovie = movieRepo.getShows(currentCity, movie.id)
                                currentScreen = Screen.MOVIE_DETAIL
                            }
                        },
                        onSearchClick = { currentScreen = Screen.SEARCH },
                        onOffersClick = { currentScreen = Screen.OFFERS }
                    )
                }

                Screen.SEARCH -> {
                    SearchScreen(
                        movies = movies,
                        onMovieClick = { movie ->
                            selectedMovie = movie
                            coroutineScope.launch {
                                showsForSelectedMovie = movieRepo.getShows(currentCity, movie.id)
                                currentScreen = Screen.MOVIE_DETAIL
                            }
                        }
                    )
                }

                Screen.MOVIE_DETAIL -> {
                    val movie = selectedMovie ?: movies.firstOrNull()
                    if (movie != null) {
                        val isWatchlisted = watchlist.any { it.movieId == movie.id }
                        MovieDetailScreen(
                            movie = movie,
                            cinemas = cinemas,
                            shows = showsForSelectedMovie,
                            isWatchlisted = isWatchlisted,
                            onToggleWatchlist = {
                                val item = WatchlistItem(
                                    movieId = movie.id,
                                    movieTitle = movie.title,
                                    posterUrl = movie.posterUrl,
                                    targetPrice = 250,
                                    preferredCinema = cinemas.firstOrNull()?.name,
                                    addedTimestamp = System.currentTimeMillis()
                                )
                                userProfileRepo.toggleWatchlist(item)
                                watchlist = userProfileRepo.getWatchlist()
                            },
                            onDateChanged = { chosenDate ->
                                coroutineScope.launch {
                                    showsForSelectedMovie = movieRepo.getShows(currentCity, movie.id, date = chosenDate)
                                }
                            },
                            onShowSelected = { show ->
                                selectedShow = show
                                currentScreen = Screen.PRICE_CALCULATOR
                            },
                            onBack = { currentScreen = Screen.HOME }
                        )
                    }
                }

                Screen.PRICE_CALCULATOR -> {
                    val show = selectedShow ?: showsForSelectedMovie.firstOrNull()
                    val movie = selectedMovie ?: movies.firstOrNull()
                    val cinema = cinemas.find { it.id == show?.cinemaId } ?: cinemas.firstOrNull()

                    if (show != null && movie != null && cinema != null) {
                        PriceCalculatorScreen(
                            show = show,
                            movieTitle = movie.title,
                            cinemaName = cinema.name,
                            userPaymentMethods = paymentMethods,
                            onSeatSelectionClick = { s, platformId ->
                                selectedShow = s
                                selectedPlatformForSeats = platformId
                                currentScreen = Screen.SEAT_SELECTION
                            },
                            onBack = { currentScreen = Screen.MOVIE_DETAIL }
                        )
                    }
                }

                Screen.SEAT_SELECTION -> {
                    val show = selectedShow ?: showsForSelectedMovie.firstOrNull()
                    if (show != null) {
                        SeatSelectionScreen(
                            show = show,
                            platformId = selectedPlatformForSeats,
                            onSeatsConfirmed = { confirmedSeats ->
                                currentScreen = Screen.PRICE_CALCULATOR
                            },
                            onBack = { currentScreen = Screen.PRICE_CALCULATOR }
                        )
                    }
                }

                Screen.OFFERS -> {
                    OffersWalletScreen(
                        offers = offers,
                        isSyncing = isSyncingOffers,
                        lastSyncTime = lastOffersSyncTime,
                        onSyncClick = {
                            coroutineScope.launch {
                                isSyncingOffers = true
                                val result = offerRepo.syncLiveOffers()
                                if (result.offers.isNotEmpty()) {
                                    offers = result.offers
                                }
                                lastOffersSyncTime = result.syncedAt
                                isSyncingOffers = false
                            }
                        }
                    )
                }

                Screen.BOOKINGS -> {
                    BookingsAnalyticsScreen(bookings = userProfileRepo.getBookings())
                }

                Screen.PROFILE -> {
                    PaymentProfileScreen(
                        paymentMethods = paymentMethods,
                        onToggleMethod = { id ->
                            userProfileRepo.togglePaymentMethod(id)
                            paymentMethods = userProfileRepo.getPaymentMethods()
                        },
                        onAddMethod = { method ->
                            userProfileRepo.addPaymentMethod(method)
                            paymentMethods = userProfileRepo.getPaymentMethods()
                        }
                    )
                }

                Screen.WATCHLIST -> {
                    WatchlistScreen(
                        watchlist = watchlist,
                        onRemoveFromWatchlist = { item ->
                            userProfileRepo.toggleWatchlist(item)
                            watchlist = userProfileRepo.getWatchlist()
                        },
                        onMovieClick = { movieId ->
                            val movie = movies.find { it.id == movieId }
                            if (movie != null) {
                                selectedMovie = movie
                                coroutineScope.launch {
                                    showsForSelectedMovie = movieRepo.getShows(currentCity, movie.id)
                                    currentScreen = Screen.MOVIE_DETAIL
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}
