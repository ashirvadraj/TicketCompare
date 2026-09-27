package com.ticketcompare.movies.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ticketcompare.movies.data.model.Cinema
import com.ticketcompare.movies.data.model.DiagnosticsSummary
import com.ticketcompare.movies.data.model.Movie
import com.ticketcompare.movies.data.model.ProviderFetchState
import com.ticketcompare.movies.data.util.DateUtils
import com.ticketcompare.movies.ui.components.CitySelectionSheet
import com.ticketcompare.movies.ui.components.MovieCard
import com.ticketcompare.movies.ui.theme.CinemaGold
import com.ticketcompare.movies.ui.theme.CrimsonAlert
import com.ticketcompare.movies.ui.theme.ElectricIndigo
import com.ticketcompare.movies.ui.theme.EmeraldSavings

@Composable
fun HomeScreen(
    currentCity: String,
    onCityChanged: (String) -> Unit,
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    movies: List<Movie>,
    cinemas: List<Cinema>,
    fetchState: ProviderFetchState,
    diagnostics: DiagnosticsSummary?,
    onMovieClick: (Movie) -> Unit,
    onSearchClick: () -> Unit,
    onOffersClick: () -> Unit,
    onRefreshClick: () -> Unit,
    onDebugClick: () -> Unit,
    isRefreshing: Boolean,
    lastUpdatedSeconds: Long
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

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Diagnostics Debug Action
                        IconButton(
                            onClick = onDebugClick,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.BugReport,
                                contentDescription = "Live Data Debug",
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

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

        // DYNAMIC 4-STATE LIVE INVENTORY INDICATOR
        item {
            val statusColor = when (fetchState) {
                ProviderFetchState.LIVE_ACTIVE -> EmeraldSavings
                ProviderFetchState.LOADING -> CinemaGold
                ProviderFetchState.NO_SHOWS_FOUND -> Color.Gray
                ProviderFetchState.DATA_UNAVAILABLE -> CrimsonAlert
                ProviderFetchState.IDLE -> if (movies.isNotEmpty()) EmeraldSavings else Color.Gray
            }

            val statusText = when (fetchState) {
                ProviderFetchState.LIVE_ACTIVE -> if (lastUpdatedSeconds > 0) "● Live Showtimes Active (${lastUpdatedSeconds}s ago)" else "● Live Showtimes Active"
                ProviderFetchState.LOADING -> "● Updating Showtimes..."
                ProviderFetchState.NO_SHOWS_FOUND -> "● No Shows Available"
                ProviderFetchState.DATA_UNAVAILABLE -> "● Live Showtimes Unavailable"
                ProviderFetchState.IDLE -> if (movies.isNotEmpty()) "● Live Showtimes Active" else "● Live Showtimes Standby"
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.clickable { onDebugClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(statusColor, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = statusText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor
                    )
                }

                Row(
                    modifier = Modifier
                        .clickable(enabled = !isRefreshing && fetchState != ProviderFetchState.LOADING) { onRefreshClick() }
                        .padding(vertical = 4.dp, horizontal = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isRefreshing || fetchState == ProviderFetchState.LOADING) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(13.dp),
                            strokeWidth = 2.dp,
                            color = CinemaGold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Syncing...",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CinemaGold
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh",
                            tint = CinemaGold,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Refresh",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = CinemaGold
                        )
                    }
                }
            }
        }

        // DYNAMIC DATE SELECTOR STRIP
        item {
            Column(modifier = Modifier.padding(bottom = 6.dp)) {
                Text(
                    text = "SELECT BOOKING DATE",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(7) { offset ->
                        val dateIso = DateUtils.getDynamicDateStr(offset)
                        val label = DateUtils.getDayDisplayLabel(offset)
                        val isSelected = selectedDate == dateIso
                        Box(
                            modifier = Modifier
                                .background(
                                    if (isSelected) CinemaGold else MaterialTheme.colorScheme.surface,
                                    RoundedCornerShape(10.dp)
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) CinemaGold else MaterialTheme.colorScheme.outline,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { onDateSelected(dateIso) }
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = label,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // SMART COMPARISON BANNER
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
                    .background(ElectricIndigo.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
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

        // PIPELINE STATE CARDS
        when (fetchState) {
            ProviderFetchState.DATA_UNAVAILABLE -> {
                // PARTIAL / TOTAL FAILURE: Clear explanation with Provider Status breakdown
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, CrimsonAlert.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "⚠️ Live Showtimes Unavailable",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = CrimsonAlert
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "We couldn't retrieve current cinema inventory from booking providers.",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            // Provider Status Checklist
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                                    .padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text("PROVIDER STATUS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CinemaGold, letterSpacing = 0.5.sp)
                                val providers = diagnostics?.providers ?: listOf(
                                    com.ticketcompare.movies.data.model.ProviderDiagnosticItem("bms", "BookMyShow", false, 0),
                                    com.ticketcompare.movies.data.model.ProviderDiagnosticItem("district", "District", false, 0),
                                    com.ticketcompare.movies.data.model.ProviderDiagnosticItem("pvr", "PVR INOX", false, 0),
                                    com.ticketcompare.movies.data.model.ProviderDiagnosticItem("cinepolis", "Cinepolis", false, 0)
                                )
                                providers.forEach { p ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(p.name, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface)
                                        Text(if (p.isConnected) "✓ connected" else "— unavailable", fontSize = 12.sp, color = if (p.isConnected) EmeraldSavings else CrimsonAlert)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Button(
                                    onClick = onRefreshClick,
                                    colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Retry")
                                }

                                OutlinedButton(
                                    onClick = onDebugClick,
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.BugReport, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Diagnostics")
                                }
                            }
                        }
                    }
                }
            }

            ProviderFetchState.NO_SHOWS_FOUND -> {
                // GENUINE ZERO SHOWS: Providers responded successfully with 0 shows
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🎟️ No Shows Available",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No bookable shows found for $currentCity on $selectedDate.\nTry selecting another date or cinema.",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = onRefreshClick,
                                colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Refresh Showtimes")
                            }
                        }
                    }
                }
            }

            else -> {
                // LIVE ACTIVE OR IDLE WITH MOVIES
                if (movies.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🎟️ No Active Showtimes Found",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "There are no currently bookable showtimes in $currentCity for $selectedDate. Shows that have passed or sold out are filtered out.",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = onRefreshClick,
                                    colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Refresh Showtimes")
                                }
                            }
                        }
                    }
                } else {
                    // TRENDING MOVIES SECTION
                    item {
                        Column(modifier = Modifier.padding(top = 16.dp)) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Playing in Cinemas Now",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Real verified showtimes with live seats",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }
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
        }
    }
}
