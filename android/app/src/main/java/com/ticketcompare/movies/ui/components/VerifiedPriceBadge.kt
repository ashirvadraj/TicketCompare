package com.ticketcompare.movies.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ticketcompare.movies.ui.theme.CrimsonAlert
import com.ticketcompare.movies.ui.theme.EmeraldSavings

@Composable
fun VerifiedPriceBadge(
    ageSeconds: Int = 10,
    priceChangeNotice: String? = null,
    modifier: Modifier = Modifier
) {
    if (!priceChangeNotice.isNullOrBlank()) {
        // Price Changed Alert Banner
        Row(
            modifier = modifier
                .fillMaxWidth()
                .background(CrimsonAlert.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                .border(1.dp, CrimsonAlert.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Price Change Alert",
                tint = CrimsonAlert,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = priceChangeNotice,
                color = CrimsonAlert,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 16.sp
            )
        }
    } else {
        // Verified Badge
        Row(
            modifier = modifier
                .background(EmeraldSavings.copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                .border(1.dp, EmeraldSavings.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Verified Price",
                tint = EmeraldSavings,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Price verified ${ageSeconds}s ago",
                color = EmeraldSavings,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
