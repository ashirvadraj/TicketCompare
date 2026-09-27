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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
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
import com.ticketcompare.movies.data.model.Movie
import com.ticketcompare.movies.data.model.Show
import com.ticketcompare.movies.data.util.DateUtils
import com.ticketcompare.movies.ui.components.ShowtimeCard
import com.ticketcompare.movies.ui.theme.CinemaGold
import com.ticketcompare.movies.ui.theme.CrimsonAlert
import com.ticketcompare.movies.ui.theme.ElectricIndigo
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class DatePill(
    val dayName: String,
    val dateText: String,
    val isoDate: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movie: Movie,
    cinemas: List<Cinema>,
    shows: List<Show>,
    initialDate: String = DateUtils.getDynamicDateStr(0),
    isWatchlisted: Boolean,
    onToggleWatchlist: () -> Unit,
    onDateChanged: (String) -> Unit,
    onShowSelected: (Show) -> Unit,
    onBack: () -> Unit
) {
    val datePills = remember {
        val sdfDay = SimpleDateFormat("EEE", Locale.getDefault())
        val sdfDate = SimpleDateFormat("dd MMM", Locale.getDefault())
        val sdfIso = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        (0..6).map { offset ->
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, offset)
            val dayLabel = when (offset) {
                0 -> "Today"
                1 -> "Tomorrow"
                else -> sdfDay.format(cal.time)
            }
            DatePill(
                dayName = dayLabel,
                dateText = sdfDate.format(cal.time),
                isoDate = sdfIso.format(cal.time)
            )
        }
    }

    var selectedDateIso by remember { mutableStateOf(initialDate) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = movie.title,
                        maxLines = 1,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onToggleWatchlist) {
                        Icon(
                            imageVector = if (isWatchlisted) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Watchlist",
                            tint = if (isWatchlisted) CrimsonAlert else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // MOVIE BANNER & META
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = CinemaGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${movie.rating} / 10",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "(${movie.voteCount} ratings)",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        }

                        // Certification
                        Box(
                            modifier = Modifier
                                .background(ElectricIndigo.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = movie.certification,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ElectricIndigo
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "${movie.durationMinutes} mins • ${movie.genre.joinToString(", ")}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Languages: ${movie.languages.joinToString(", ")}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = movie.synopsis,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f),
                        lineHeight = 18.sp
                    )
                }
            }

            // DYNAMIC DATE STRIP PICKER
            item {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    Text(
                        text = "SELECT BOOKING DATE",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                    )

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(datePills) { pill ->
                            val isSelected = selectedDateIso == pill.isoDate
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
                                    .clickable {
                                        selectedDateIso = pill.isoDate
                                        onDateChanged(pill.isoDate)
                                    }
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = pill.dayName,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = pill.dateText,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // SHOWTIMES PER CINEMA
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Available Cinemas & Live Price Comparisons",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    val cinemasWithShows = cinemas.filter { cinema -> shows.any { it.cinemaId == cinema.id } }

                    if (cinemasWithShows.isEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "No shows scheduled for $selectedDateIso",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Shows that have already started, sold out, or not yet opened for booking are omitted. Please try another date above.",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    } else {
                        cinemasWithShows.forEach { cinema ->
                            val cinemaShows = shows.filter { it.cinemaId == cinema.id }
                            if (cinemaShows.isNotEmpty()) {
                                ShowtimeCard(
                                    cinema = cinema,
                                    shows = cinemaShows,
                                    onShowSelected = onShowSelected
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
