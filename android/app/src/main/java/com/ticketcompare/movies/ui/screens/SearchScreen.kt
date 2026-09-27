package com.ticketcompare.movies.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ticketcompare.movies.data.model.Movie
import com.ticketcompare.movies.ui.theme.CinemaGold
import com.ticketcompare.movies.ui.theme.ElectricIndigo
import com.ticketcompare.movies.ui.theme.EmeraldSavings

@Composable
fun SearchScreen(
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFormat by remember { mutableStateOf("ALL") }
    var selectedLanguage by remember { mutableStateOf("ALL") }

    val formats = listOf("ALL", "IMAX 3D", "3D", "2D", "4DX")
    val languages = listOf("ALL", "Hindi", "English", "Telugu", "Tamil")

    val filteredMovies = movies.filter { movie ->
        val matchesQuery = searchQuery.isBlank() || 
            movie.title.contains(searchQuery, ignoreCase = true) ||
            movie.genre.any { it.contains(searchQuery, ignoreCase = true) }
        
        val matchesFormat = selectedFormat == "ALL" || movie.formats.any { it.contains(selectedFormat, ignoreCase = true) }
        val matchesLang = selectedLanguage == "ALL" || movie.languages.contains(selectedLanguage)

        matchesQuery && matchesFormat && matchesLang
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Search Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Search Movies & Formats",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Search Input Box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
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
                BasicTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    singleLine = true,
                    textStyle = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    cursorBrush = SolidColor(CinemaGold),
                    modifier = Modifier.weight(1f),
                    decorationBox = { innerTextField ->
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Search Avatar, War 2, IMAX...",
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                fontSize = 15.sp
                            )
                        }
                        innerTextField()
                    }
                )
                if (searchQuery.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier
                            .size(18.dp)
                            .clickable { searchQuery = "" }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Format Filters
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(formats) { fmt ->
                    val isSelected = selectedFormat == fmt
                    Box(
                        modifier = Modifier
                            .background(
                                if (isSelected) ElectricIndigo else MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(8.dp)
                            )
                            .border(
                                1.dp,
                                if (isSelected) ElectricIndigo else MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedFormat = fmt }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = fmt,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Language Filters
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(languages) { lang ->
                    val isSelected = selectedLanguage == lang
                    Box(
                        modifier = Modifier
                            .background(
                                if (isSelected) CinemaGold.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(8.dp)
                            )
                            .border(
                                1.dp,
                                if (isSelected) CinemaGold else MaterialTheme.colorScheme.outline,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedLanguage = lang }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = lang,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) CinemaGold else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // Search Results List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = "${filteredMovies.size} Bookable Movies Found",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    letterSpacing = 0.5.sp
                )
            }

            if (filteredMovies.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
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
                                text = "No Active Cinema Shows Found",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Only movies with currently active, future bookable showtimes are displayed. Titles without live cinema slots are omitted.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            items(filteredMovies) { movie ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                        .clickable { onMovieClick(movie) }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Movie Box
                    Box(
                        modifier = Modifier
                            .size(70.dp, 95.dp)
                            .background(ElectricIndigo.copy(alpha = 0.25f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = movie.title.take(2).uppercase(),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = ElectricIndigo
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = movie.title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${movie.certification} • ${movie.durationMinutes}m • ${movie.genre.joinToString(", ")}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Text(
                            text = movie.formats.joinToString(" • "),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ElectricIndigo,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Compare Prices: ",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                            Text(
                                text = "From ₹189 final",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = EmeraldSavings
                            )
                        }
                    }
                }
            }
        }
    }
}
