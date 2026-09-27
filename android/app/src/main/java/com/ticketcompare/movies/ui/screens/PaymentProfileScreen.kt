package com.ticketcompare.movies.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ticketcompare.movies.data.model.SavedPaymentMethod
import com.ticketcompare.movies.ui.theme.CinemaGold
import com.ticketcompare.movies.ui.theme.ElectricIndigo
import com.ticketcompare.movies.ui.theme.EmeraldSavings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentProfileScreen(
    paymentMethods: List<SavedPaymentMethod>,
    onToggleMethod: (String) -> Unit,
    onAddMethod: (SavedPaymentMethod) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedBank by remember { mutableStateOf("Axis Bank") }
    var selectedCardType by remember { mutableStateOf("CREDIT") }

    val bankOptions = listOf("Axis Bank", "Kotak", "IDFC FIRST", "RBL Bank", "IndusInd", "YES BANK", "Amex")

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add Payment Method", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Select only bank name and card type. We NEVER request or store sensitive card numbers or CVVs.",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text("Bank:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    bankOptions.forEach { b ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedBank = b }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = selectedBank == b, onClick = { selectedBank = b })
                            Text(text = b, fontSize = 13.sp)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onAddMethod(
                            SavedPaymentMethod(
                                id = "pm-${selectedBank.lowercase().replace(" ", "")}-${System.currentTimeMillis()}",
                                category = "CREDIT_CARD",
                                bank = selectedBank,
                                cardType = selectedCardType,
                                cardNetwork = "VISA",
                                isSelected = true
                            )
                        )
                        showAddDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo)
                ) {
                    Text("Add Method")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Cancel") }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "My Payment Methods",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Tick the payment instruments you own to personalize discounts automatically.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // HARDENED SECURITY NOTICE
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(EmeraldSavings.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                    .border(1.dp, EmeraldSavings.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = "Zero-Knowledge Security",
                    tint = EmeraldSavings,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Zero-Knowledge Card Privacy",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldSavings
                    )
                    Text(
                        text = "TicketCompare NEVER asks for or stores card numbers, CVV, OTP, UPI PINs, or banking credentials. Only non-sensitive bank tier metadata is encrypted locally using Android Keystore.",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        lineHeight = 15.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SAVED INSTRUMENTS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    letterSpacing = 1.sp
                )

                TextButton(onClick = { showAddDialog = true }) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = CinemaGold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Bank Card", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = CinemaGold)
                }
            }
        }

        items(paymentMethods) { method ->
            val isChecked = method.isSelected
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                    .border(
                        1.dp,
                        if (isChecked) ElectricIndigo.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outline,
                        RoundedCornerShape(12.dp)
                    )
                    .clickable { onToggleMethod(method.id) }
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = when (method.category) {
                            "CREDIT_CARD" -> "${method.bank} Credit Card ${if (!method.cardTier.isNullOrEmpty()) "(${method.cardTier})" else ""}"
                            "DEBIT_CARD" -> "${method.bank} Debit Card"
                            "UPI" -> "${method.providerApp?.replace("_", " ")} UPI"
                            else -> method.bank ?: "Payment Method"
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (isChecked) "✓ Active in price calculations" else "Inactive",
                        fontSize = 11.sp,
                        color = if (isChecked) EmeraldSavings else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { onToggleMethod(method.id) },
                    colors = CheckboxDefaults.colors(checkedColor = ElectricIndigo)
                )
            }
        }
    }
}
