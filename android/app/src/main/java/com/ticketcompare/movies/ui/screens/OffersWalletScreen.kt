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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ticketcompare.movies.data.model.Offer
import com.ticketcompare.movies.ui.theme.CinemaGold
import com.ticketcompare.movies.ui.theme.ElectricIndigo
import com.ticketcompare.movies.ui.theme.EmeraldSavings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OffersWalletScreen(
    offers: List<Offer>,
    isSyncing: Boolean = false,
    lastSyncTime: Long = System.currentTimeMillis(),
    onSyncClick: () -> Unit = {}
) {
    var selectedCategory by remember { mutableStateOf("ALL") }
    var selectedBank by remember { mutableStateOf("ALL") }
    var selectedOfferForTerms by remember { mutableStateOf<Offer?>(null) }

    val categories = listOf("ALL", "CREDIT_CARD", "DEBIT_CARD", "UPI", "WALLET", "COUPON", "MEMBERSHIP")
    val banks = listOf("ALL", "ICICI", "HDFC", "Axis", "SBI", "Kotak", "RBL", "IDFC", "IndusInd", "AU", "UPI")

    val filteredOffers = offers.filter { offer ->
        val matchesCategory = if (selectedCategory == "ALL") true else offer.category == selectedCategory
        val matchesBank = if (selectedBank == "ALL") {
            true
        } else if (selectedBank == "UPI") {
            offer.category == "UPI" || offer.category == "WALLET"
        } else {
            offer.bank?.contains(selectedBank, ignoreCase = true) == true
        }
        matchesCategory && matchesBank
    }

    if (selectedOfferForTerms != null) {
        val o = selectedOfferForTerms!!
        AlertDialog(
            onDismissRequest = { selectedOfferForTerms = null },
            title = {
                Text(text = o.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = "Source: ${o.source}", fontSize = 12.sp, color = ElectricIndigo, fontWeight = FontWeight.SemiBold)
                    Text(text = "Minimum Order: ₹${o.minTransaction}", fontSize = 13.sp)
                    Text(text = "Valid on: ${o.applicablePlatforms.joinToString(", ").uppercase()}", fontSize = 13.sp)
                    Divider(color = MaterialTheme.colorScheme.outline)
                    Text(text = "TERMS & CONDITIONS:", fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    o.terms.forEach { term ->
                        Text(text = "• $term", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedOfferForTerms = null }) {
                    Text("Close", fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // TOP HEADER
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Real-Time Offers & Bank Deals",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Live feeds from BookMyShow, District, PVR INOX & Cinepolis",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                // SYNC ACTION BUTTON
                Button(
                    onClick = onSyncClick,
                    enabled = !isSyncing,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (isSyncing) {
                        CircularProgressIndicator(modifier = Modifier.size(14.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Icon(imageVector = Icons.Default.Sync, contentDescription = "Sync", modifier = Modifier.size(14.dp), tint = Color.White)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Sync", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // LIVE STATUS BADGE
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(EmeraldSavings.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSavings, modifier = Modifier.size(12.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "🟢 Live Synced: ${offers.size} active bank deals & distributor coupons verified",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = EmeraldSavings
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // CATEGORY TABS
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    val displayLabel = when (cat) {
                        "CREDIT_CARD" -> "Credit Cards"
                        "DEBIT_CARD" -> "Debit Cards"
                        "UPI" -> "UPI"
                        "WALLET" -> "Wallets"
                        "COUPON" -> "Coupons"
                        "MEMBERSHIP" -> "Memberships"
                        else -> "All Types"
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                if (isSelected) CinemaGold else MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = displayLabel,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // BANK FILTER CHIPS
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(banks) { b ->
                    val isSelected = selectedBank == b
                    val displayBank = when (b) {
                        "ALL" -> "All Banks"
                        "ICICI" -> "ICICI"
                        "HDFC" -> "HDFC"
                        "Axis" -> "Axis"
                        "SBI" -> "SBI Card"
                        "Kotak" -> "Kotak"
                        "RBL" -> "RBL"
                        "IDFC" -> "IDFC FIRST"
                        "IndusInd" -> "IndusInd"
                        "AU" -> "AU Bank"
                        "UPI" -> "UPI / Wallets"
                        else -> b
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                if (isSelected) ElectricIndigo else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                RoundedCornerShape(16.dp)
                            )
                            .border(
                                1.dp,
                                if (isSelected) ElectricIndigo else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f),
                                RoundedCornerShape(16.dp)
                            )
                            .clickable { selectedBank = b }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = displayBank,
                            fontSize = 10.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // OFFERS LIST
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredOffers) { offer ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Category Pill & Bank Name
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            if (!offer.bank.isNullOrEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .background(CinemaGold.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                    Text(
                                        text = offer.bank,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = CinemaGold
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .background(
                                        if (offer.isCashback) CinemaGold.copy(alpha = 0.15f) else ElectricIndigo.copy(alpha = 0.15f),
                                        RoundedCornerShape(6.dp)
                                    )
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = if (offer.isCashback) "Post-Payment Cashback" else "Instant Discount",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (offer.isCashback) CinemaGold else ElectricIndigo
                                )
                            }
                        }

                        // Verified Status
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSavings, modifier = Modifier.size(11.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(text = "Verified Live", fontSize = 10.sp, color = EmeraldSavings, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = offer.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = offer.description,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Min Spend: ₹${offer.minTransaction}",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Text(
                                text = "Platforms: ${offer.applicablePlatforms.joinToString(", ").uppercase()}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        }

                        TextButton(onClick = { selectedOfferForTerms = offer }) {
                            Text("View Terms", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = CinemaGold)
                        }
                    }
                }
            }
        }
    }
}
