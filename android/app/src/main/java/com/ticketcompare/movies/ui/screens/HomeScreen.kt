package com.ticketcompare.movies.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ticketcompare.movies.data.model.Cinema
import com.ticketcompare.movies.data.model.Movie
import com.ticketcompare.movies.ui.components.CitySelectionSheet
import com.ticketcompare.movies.ui.components.MovieCard
import com.ticketcompare.movies.ui.theme.CinemaGold
import com.ticketcompare.movies.ui.theme.ElectricIndigo
import com.ticketcompare.movies.ui.theme.EmeraldSavings

@Composable
fun HomeScreen(
    currentCity: String,
    onCityChanged: (String) -> Unit,
    movies: List<Movie>,
    cinemas: List<Cinema>,
    onMovieClick: (Movie) -> Unit,
    onSearchClick: () -> Unit,
    onOffersClick: () -> Unit
) {
    var showCitySheet by remember { mutableStateOf(false) }

    if (showCitySheet) {
        CitySelectionSheet(
            currentCity = currentCity,
            onCitySelected = { onCityChanged(it) },
            onDismiss = { showCitySheet = false }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // TOP APP BAR: City & Search
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                // Location Selector Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clickable { showCitySheet = true }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = CinemaGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = currentCity,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Expand",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Offers Quick Pill
                    Row(
                        modifier = Modifier
                            .background(CinemaGold.copy(alpha = 0.15f), RoundedCornerShape(999.dp))
                            .border(1.dp, CinemaGold.copy(alpha = 0.4f), RoundedCornerShape(999.dp))
                            .clickable { onOffersClick() }
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalOffer,
                            contentDescription = "Offers",
                            tint = CinemaGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Save ₹150+",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = CinemaGold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Search Bar Trigger
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                        .clickable { onSearchClick() }
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Search movies, cinemas, or formats...",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }
        }

        // QUICK FILTER CHIPS
        item {
            val todayStr = remember { java.text.SimpleDateFormat("dd MMM", java.util.Locale.getDefault()).format(java.util.Calendar.getInstance().time) }
            val tomorrowStr = remember {
                val cal = java.util.Calendar.getInstance()
                cal.add(java.util.Calendar.DAY_OF_YEAR, 1)
                java.text.SimpleDateFormat("dd MMM", java.util.Locale.getDefault()).format(cal.time)
            }
            val chips = listOf("🔥 In Cinemas Now", "📅 Today ($todayStr)", "🚀 Tomorrow ($tomorrowStr)", "🌟 IMAX 3D", "🕶️ 3D", "🇮🇳 Hindi", "🇬🇧 English")
            LazyRow(
                modifier = Modifier.padding(vertical = 14.dp),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(chips) { chip ->
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(8.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                            .clickable { onSearchClick() }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = chip,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // FEATURED COMPARISON BANNER
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
                    .background(
                        ElectricIndigo.copy(alpha = 0.15f),
                        RoundedCornerShape(16.dp)
                    )
                    .border(1.dp, ElectricIndigo.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "SMART PRICE COMPARISON",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp,
                            color = ElectricIndigo
                        )
                        Text(
                            text = "Save up to 40%",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldSavings
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Compare Complete Final Payable Amount",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "We calculate base ticket + convenience fees + 18% GST minus your bank cards, coupons & UPI discounts.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // TRENDING MOVIES SECTION
        item {
            Column(modifier = Modifier.padding(top = 20.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Trending Movies",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "See All",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = CinemaGold,
                        modifier = Modifier.clickable { onSearchClick() }
                    )
                }

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(movies) { movie ->
                        MovieCard(
                            movie = movie,
                            onClick = { onMovieClick(movie) }
                        )
                    }
                }
            }
        }

        // NEARBY CINEMAS IN CITY
        item {
            Column(modifier = Modifier.padding(top = 24.dp, start = 20.dp, end = 20.dp)) {
                Text(
                    text = "Nearby Cinemas in $currentCity",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                cinemas.forEach { cinema ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                            .clickable {
                                val firstMovie = movies.firstOrNull()
                                if (firstMovie != null) onMovieClick(firstMovie)
                            }
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = cinema.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${cinema.distanceKm} km • ${cinema.supportedPlatforms.map { it.uppercase() }.joinToString(" • ")}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                            Text(
                                text = "View Shows",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = CinemaGold
                            )
                        }
                    }
                }
            }
        }
    }
}
