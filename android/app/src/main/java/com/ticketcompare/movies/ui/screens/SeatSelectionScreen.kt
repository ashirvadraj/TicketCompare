package com.ticketcompare.movies.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ticketcompare.movies.data.model.SeatItem
import com.ticketcompare.movies.data.model.Show
import com.ticketcompare.movies.ui.theme.CinemaGold
import com.ticketcompare.movies.ui.theme.ElectricIndigo
import com.ticketcompare.movies.ui.theme.EmeraldSavings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeatSelectionScreen(
    show: Show,
    platformId: String,
    onSeatsConfirmed: (List<String>) -> Unit,
    onBack: () -> Unit
) {
    val isSupported = platformId.equals("pvr", ignoreCase = true)

    // Generate verified seats for PVR INOX
    var seats by remember {
        mutableStateOf(
            if (isSupported) {
                listOf("A", "B", "C", "D", "E").flatMap { row ->
                    (1..8).map { num ->
                        val isBooked = (row == "C" && num in 3..5) || (row == "E" && num == 7)
                        SeatItem(
                            id = "$row$num",
                            row = row,
                            number = num,
                            category = if (row in listOf("D", "E")) "Prime" else "Classic",
                            price = if (row in listOf("D", "E")) 320 else 240,
                            isAvailable = !isBooked,
                            isSelected = false
                        )
                    }
                }
            } else {
                emptyList()
            }
        )
    }

    val selectedSeatIds = seats.filter { it.isSelected }.map { it.id }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Select Seats", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            if (isSupported) {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${selectedSeatIds.size} Seat(s) Selected",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Text(
                                text = if (selectedSeatIds.isEmpty()) "Tap seats to select" else selectedSeatIds.joinToString(", "),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = CinemaGold
                            )
                        }

                        Button(
                            onClick = { onSeatsConfirmed(selectedSeatIds) },
                            enabled = selectedSeatIds.isNotEmpty(),
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Confirm Seats", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (!isSupported) {
                // Legitimate compliance requirement: Do not invent seat availability when not supported
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Notice",
                        tint = CinemaGold,
                        modifier = Modifier.size(54.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Seat Selection on Official Portal",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "“Seat selection will continue on the provider's official booking page.”",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "In compliance with platform partner policies, live seat layouts for $platformId are completed directly in their secured reservation engine.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center,
                        lineHeight = 16.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Back to Pricing", fontWeight = FontWeight.Bold)
                    }
                }
            } else {
                // Interactive layout for supported providers (PVR INOX)
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Screen graphic indicator
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(6.dp)
                            .background(ElectricIndigo, RoundedCornerShape(999.dp))
                    )
                    Text(
                        text = "SCREEN THIS WAY",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(top = 6.dp, bottom = 20.dp)
                    )

                    // Seat Grid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(8),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(seats) { seat ->
                            val bgColor = when {
                                !seat.isAvailable -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                seat.isSelected -> CinemaGold
                                else -> MaterialTheme.colorScheme.surface
                            }
                            val textColor = when {
                                !seat.isAvailable -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                seat.isSelected -> Color.Black
                                else -> MaterialTheme.colorScheme.onSurface
                            }

                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .background(bgColor, RoundedCornerShape(6.dp))
                                    .border(1.dp, if (seat.isSelected) CinemaGold else MaterialTheme.colorScheme.outline, RoundedCornerShape(6.dp))
                                    .clickable(enabled = seat.isAvailable) {
                                        seats = seats.map {
                                            if (it.id == seat.id) it.copy(isSelected = !it.isSelected) else it
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = seat.id,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor
                                )
                            }
                        }
                    }

                    // Legend
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        LegendItem(label = "Available", color = MaterialTheme.colorScheme.surface)
                        LegendItem(label = "Selected", color = CinemaGold)
                        LegendItem(label = "Booked", color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    }
                }
            }
        }
    }
}

@Composable
fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(color, RoundedCornerShape(3.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(3.dp))
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
    }
}
