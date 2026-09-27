package com.ticketcompare.movies.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ticketcompare.movies.data.model.PriceBreakdown
import com.ticketcompare.movies.ui.theme.CinemaGold
import com.ticketcompare.movies.ui.theme.EmeraldSavings

@Composable
fun PriceBreakdownCard(
    breakdown: PriceBreakdown,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
            .padding(18.dp)
    ) {
        // Platform Header & Age
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = breakdown.platformName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${breakdown.ticketCount} Seat(s) • ${breakdown.seatCategory}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            VerifiedPriceBadge(ageSeconds = breakdown.verificationAgeSeconds)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Price Breakdown Items
        BreakdownRow(label = "Base Ticket Price", amount = "₹${breakdown.basePrice}")
        BreakdownRow(label = "Convenience Fee", amount = "₹${breakdown.convenienceFee}")
        BreakdownRow(label = "Internet Handling Fee", amount = "₹${breakdown.internetHandlingFee}")
        BreakdownRow(label = "GST on Fees (18%)", amount = "₹${breakdown.gstOnFees}")

        if (breakdown.totalDiscount > 0) {
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(8.dp))

            if (breakdown.couponDiscount > 0) {
                BreakdownRow(
                    label = "Coupon (${breakdown.appliedCouponCode ?: "PROMO"})",
                    amount = "-₹${breakdown.couponDiscount}",
                    isDiscount = true
                )
            }
            if (breakdown.bankDiscount > 0) {
                BreakdownRow(
                    label = "Bank Card Instant Discount",
                    amount = "-₹${breakdown.bankDiscount}",
                    isDiscount = true
                )
            }
            if (breakdown.upiDiscount > 0) {
                BreakdownRow(
                    label = "UPI Instant Discount",
                    amount = "-₹${breakdown.upiDiscount}",
                    isDiscount = true
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Divider(color = MaterialTheme.colorScheme.outline)
        Spacer(modifier = Modifier.height(12.dp))

        // FINAL PAYABLE AMOUNT
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "FINAL PAYABLE",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp,
                    color = CinemaGold
                )
                Text(
                    text = "Amount deducted at checkout",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
            Text(
                text = "₹${breakdown.finalPayableAmount}",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Separate Cashback Section (if applicable)
        if (breakdown.potentialCashback > 0) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(EmeraldSavings.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                    .border(1.dp, EmeraldSavings.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "+ ₹${breakdown.potentialCashback} Expected Cashback",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldSavings
                    )
                    Text(
                        text = "Credited post-payment to bank/wallet",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Effective Cost",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "₹${breakdown.effectiveCost}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldSavings
                    )
                }
            }
        }
    }
}

@Composable
fun BreakdownRow(
    label: String,
    amount: String,
    isDiscount: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = if (isDiscount) EmeraldSavings else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            fontWeight = if (isDiscount) FontWeight.SemiBold else FontWeight.Normal
        )
        Text(
            text = amount,
            fontSize = 13.sp,
            color = if (isDiscount) EmeraldSavings else MaterialTheme.colorScheme.onSurface,
            fontWeight = if (isDiscount) FontWeight.Bold else FontWeight.Medium
        )
    }
}
