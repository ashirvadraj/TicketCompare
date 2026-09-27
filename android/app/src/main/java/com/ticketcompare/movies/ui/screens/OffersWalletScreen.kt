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
    offers: List<Offer>
) {
    var selectedCategory by remember { mutableStateOf("ALL") }
    var selectedOfferForTerms by remember { mutableStateOf<Offer?>(null) }

    val categories = listOf("ALL", "CREDIT_CARD", "DEBIT_CARD", "UPI", "WALLET", "COUPON", "MEMBERSHIP")

    val filteredOffers = if (selectedCategory == "ALL") offers else offers.filter { it.category == selectedCategory }

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
        // HEADER
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Offer Wallet & Verified Deals",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Discover eligible bank cards, UPI offers, and authorized coupons.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // CATEGORY TABS
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    val isSelected = selectedCategory == cat
                    val displayLabel = when (cat) {
                        "CREDIT_CARD" -> "Credit Cards"
                        "DEBIT_CARD" -> "Debit Cards"
                        "UPI" -> "UPI Offers"
                        "WALLET" -> "Wallets"
                        "COUPON" -> "Coupons"
                        "MEMBERSHIP" -> "Memberships"
                        else -> "All Deals"
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                if (isSelected) CinemaGold else MaterialTheme.colorScheme.surfaceVariant,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = displayLabel,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }

        // OFFERS LIST
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(filteredOffers) { offer ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(14.dp))
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(14.dp))
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Category & Type Pill
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (offer.isCashback) CinemaGold.copy(alpha = 0.15f) else ElectricIndigo.copy(alpha = 0.15f),
                                        RoundedCornerShape(6.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = if (offer.isCashback) "Cashback After Payment" else "Instant Discount",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (offer.isCashback) CinemaGold else ElectricIndigo
                                )
                            }
                        }

                        // Verified Age
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSavings, modifier = Modifier.size(12.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Verified 5m ago", fontSize = 10.sp, color = EmeraldSavings, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

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

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Min Spend: ₹${offer.minTransaction}",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                            Text(
                                text = "Applicable on: ${offer.applicablePlatforms.joinToString(", ").uppercase()}",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                            )
                        }

                        TextButton(onClick = { selectedOfferForTerms = offer }) {
                            Text("View Terms", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CinemaGold)
                        }
                    }
                }
            }
        }
    }
}
