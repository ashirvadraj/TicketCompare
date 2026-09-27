package com.ticketcompare.movies.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ticketcompare.movies.data.model.SavedPaymentMethod
import com.ticketcompare.movies.data.model.Show
import com.ticketcompare.movies.data.repository.MovieRepository
import com.ticketcompare.movies.engine.ClientPriceCalculationEngine
import com.ticketcompare.movies.ui.components.PriceBreakdownCard
import com.ticketcompare.movies.ui.components.VerifiedPriceBadge
import com.ticketcompare.movies.ui.theme.CinemaGold
import com.ticketcompare.movies.ui.theme.ElectricIndigo
import com.ticketcompare.movies.ui.theme.EmeraldSavings
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceCalculatorScreen(
    show: Show,
    movieTitle: String,
    cinemaName: String,
    userPaymentMethods: List<SavedPaymentMethod>,
    movieRepo: MovieRepository,
    onSeatSelectionClick: (Show, String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var selectedPlatformId by remember { mutableStateOf(show.cheapestPlatformId) }
    var ticketCount by remember { mutableStateOf(1) }
    var seatCategory by remember { mutableStateOf("Classic") }
    var couponInput by remember { mutableStateOf("") }
    var appliedCoupon by remember { mutableStateOf<String?>(null) }
    var couponMessage by remember { mutableStateOf<String?>(null) }
    var priceChangeNotice by remember { mutableStateOf<String?>(null) }
    var isVerifyingBooking by remember { mutableStateOf(false) }
    var unavailableDialogMessage by remember { mutableStateOf<String?>(null) }

    val currentPlatformPrice = show.pricing.find { it.platformId == selectedPlatformId } ?: show.pricing.first()

    // Real-time calculated price breakdown using dedicated engine
    val breakdown = remember(selectedPlatformId, ticketCount, seatCategory, appliedCoupon, userPaymentMethods) {
        ClientPriceCalculationEngine.calculate(
            platformId = selectedPlatformId,
            platformName = currentPlatformPrice.platformName,
            baseTicketPrice = currentPlatformPrice.ticketPrice,
            ticketCount = ticketCount,
            seatCategory = seatCategory,
            userPaymentMethods = userPaymentMethods,
            enteredCouponCode = appliedCoupon
        )
    }

    if (unavailableDialogMessage != null) {
        AlertDialog(
            onDismissRequest = { unavailableDialogMessage = null },
            title = {
                Text(text = "Showtime Unavailable", fontWeight = FontWeight.Bold)
            },
            text = {
                Text(text = unavailableDialogMessage ?: "This show is no longer available. Please select another show.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        unavailableDialogMessage = null
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo)
                ) {
                    Text("Select Another Show")
                }
            },
            dismissButton = {
                TextButton(onClick = { unavailableDialogMessage = null }) {
                    Text("Dismiss")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Final Price Calculator",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            // STICKY BOTTOM CHECKOUT ACTION
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "FINAL PAYABLE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = CinemaGold
                            )
                            Text(
                                text = "₹${breakdown.finalPayableAmount}",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    isVerifyingBooking = true
                                    val verification = movieRepo.verifyShowAvailability(show.id)
                                    isVerifyingBooking = false
                                    if (!verification.isBookable) {
                                        unavailableDialogMessage = verification.message.ifBlank { "This show is no longer available. Please select another show." }
                                    } else {
                                        // Launch official booking deep link or web checkout
                                        val targetUrl = if (currentPlatformPrice.deepLink.isNotBlank()) {
                                            currentPlatformPrice.deepLink
                                        } else {
                                            currentPlatformPrice.officialWebCheckout
                                        }
                                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(targetUrl))
                                        try {
                                            context.startActivity(intent)
                                        } catch (e: Exception) {
                                            val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(currentPlatformPrice.officialWebCheckout))
                                            context.startActivity(webIntent)
                                        }
                                    }
                                }
                            },
                            enabled = !isVerifyingBooking,
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(48.dp)
                        ) {
                            if (isVerifyingBooking) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Verifying...",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            } else {
                                Text(
                                    text = "Continue on ${currentPlatformPrice.platformName}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(imageVector = Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // BOOKING DETAILS SUMMARY
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = movieTitle,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "$cinemaName • ${show.time} • ${show.format}",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(if (show.availableSeats > 20) EmeraldSavings else CinemaGold, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Live Inventory: ${show.availableSeats} of ${show.totalSeats} seats open",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (show.availableSeats > 20) EmeraldSavings else CinemaGold
                            )
                        }
                        Text(
                            text = "Date: ${show.date}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

            // PROVIDER SELECTOR TABS
            item {
                Text(
                    text = "SELECT BOOKING PLATFORM",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    show.pricing.forEach { p ->
                        val isSelected = p.platformId == selectedPlatformId
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(
                                    if (isSelected) ElectricIndigo.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface,
                                    RoundedCornerShape(10.dp)
                                )
                                .border(
                                    1.dp,
                                    if (isSelected) ElectricIndigo else MaterialTheme.colorScheme.outline,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { selectedPlatformId = p.platformId }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = p.platformName,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) ElectricIndigo else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "₹${p.finalPayable}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (p.platformId == show.cheapestPlatformId) CinemaGold else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // TICKET COUNT & SEAT CATEGORY CONTROLS
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Ticket Stepper
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Tickets",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            FilledIconButton(
                                onClick = { if (ticketCount > 1) ticketCount-- },
                                colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
                            }
                            Text(
                                text = "$ticketCount",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            FilledIconButton(
                                onClick = { if (ticketCount < 8) ticketCount++ },
                                colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp))
                            }
                        }
                    }

                    // Seat Category Picker
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Category",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            listOf("Classic", "Prime", "Recliner").forEach { cat ->
                                val isSelected = seatCategory == cat
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .background(
                                            if (isSelected) CinemaGold else MaterialTheme.colorScheme.surfaceVariant,
                                            RoundedCornerShape(6.dp)
                                        )
                                        .clickable { seatCategory = cat }
                                        .padding(vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = cat.take(4),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // SEAT SELECTION BUTTON (IF SUPPORTED)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
                        .clickable { onSeatSelectionClick(show, selectedPlatformId) }
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (currentPlatformPrice.seatInventorySupported) "💺 Select Live Seats" else "💺 Seat Selection Info",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (currentPlatformPrice.seatInventorySupported) "Official PVR screen layout ready" else "Continues on provider official page",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    Text(
                        text = "Choose >",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = CinemaGold
                    )
                }
            }

            // COUPON ENTRY SECTION
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Have a Promo Code or Voucher?",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = couponInput,
                            onValueChange = { couponInput = it },
                            placeholder = { Text("e.g. SAVE100 or MOVIE50", fontSize = 12.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                val clean = couponInput.trim().uppercase()
                                if (clean == "SAVE100" || clean == "MOVIE50" || clean == "PVRPASS") {
                                    appliedCoupon = clean
                                    couponMessage = "✓ Coupon $clean applied successfully!"
                                } else {
                                    couponMessage = "Invalid or expired coupon code."
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CinemaGold),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = "APPLY", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                    if (couponMessage != null) {
                        Text(
                            text = couponMessage!!,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (appliedCoupon != null) EmeraldSavings else Color.Red,
                            modifier = Modifier.padding(top = 6.dp)
                        )
                    }
                }
            }

            // PRICE VERIFICATION BANNER
            item {
                VerifiedPriceBadge(
                    ageSeconds = breakdown.verificationAgeSeconds,
                    priceChangeNotice = priceChangeNotice
                )
            }

            // DETAILED ITEMIZED BREAKDOWN CARD
            item {
                PriceBreakdownCard(breakdown = breakdown)
            }

            // PRIVACY & SECURITY LEGAL NOTICE
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = "🔒 Security Notice: TicketCompare never stores card numbers, CVVs, UPI PINs, or passwords. Your payment is securely authenticated and processed directly by ${currentPlatformPrice.platformName}.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}
