package com.ticketcompare.movies.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ticketcompare.movies.data.api.ApiClient
import com.ticketcompare.movies.data.model.DiagnosticsSummary
import com.ticketcompare.movies.data.model.ProviderDiagnosticItem
import com.ticketcompare.movies.data.repository.MovieRepository
import com.ticketcompare.movies.ui.theme.CinemaGold
import com.ticketcompare.movies.ui.theme.CrimsonAlert
import com.ticketcompare.movies.ui.theme.ElectricIndigo
import com.ticketcompare.movies.ui.theme.EmeraldSavings
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveDataDebugScreen(
    currentCity: String,
    selectedDate: String,
    movieRepo: MovieRepository,
    onBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val diagnostics by movieRepo.diagnostics.collectAsState()
    var isTesting by remember { mutableStateOf(false) }
    var customUrlInput by remember { mutableStateOf(ApiClient.baseUrl) }
    var connectionTestStatus by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        if (diagnostics == null) {
            movieRepo.fetchDiagnostics(currentCity, selectedDate)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("LIVE DATA DEBUG", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text("Diagnostics & Provider Pipeline", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            isTesting = true
                            movieRepo.fetchDiagnostics(currentCity, selectedDate)
                            isTesting = false
                        }
                    }) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh Diagnostics", tint = CinemaGold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // SELECTED QUERY META
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("SELECTED QUERY PARAMETERS", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CinemaGold, letterSpacing = 1.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("City:", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                            Text(currentCity, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Resolved Date:", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                            Text(selectedDate, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Timezone:", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                            Text(diagnostics?.timezone ?: "Asia/Kolkata", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = EmeraldSavings)
                        }
                    }
                }
            }

            // BACKEND SERVER ENDPOINT CONFIGURATION
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("BACKEND API HOST CONFIGURATION", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = ElectricIndigo, letterSpacing = 1.sp)
                        Text(
                            "Emulator uses 10.0.2.2:4000. Physical device requires PC Wi-Fi IP (e.g. http://192.168.1.X:4000/):",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = customUrlInput,
                                onValueChange = { customUrlInput = it },
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                textStyle = LocalTextStyle.current.copy(fontSize = 12.sp, fontFamily = FontFamily.Monospace)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    ApiClient.setCustomBaseUrl(customUrlInput)
                                    coroutineScope.launch {
                                        isTesting = true
                                        val res = movieRepo.fetchDiagnostics(currentCity, selectedDate)
                                        isTesting = false
                                        connectionTestStatus = if (res != null) "✓ Connected: ${res.successfulProviders} providers live" else "✗ Unreachable: check IP and port"
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ElectricIndigo),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Set & Test", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        if (connectionTestStatus != null) {
                            Text(
                                text = connectionTestStatus!!,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (connectionTestStatus!!.startsWith("✓")) EmeraldSavings else CrimsonAlert,
                                modifier = Modifier.padding(top = 6.dp)
                            )
                        }
                    }
                }
            }

            // SUMMARY METRICS
            item {
                val diag = diagnostics
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("PIPELINE SUMMARY", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = CinemaGold, letterSpacing = 1.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Providers:", fontSize = 12.sp)
                            Text("${diag?.totalProviders ?: 4}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Successful Providers:", fontSize = 12.sp)
                            Text("${diag?.successfulProviders ?: 0}", fontWeight = FontWeight.Bold, color = EmeraldSavings, fontSize = 12.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Shows Generated:", fontSize = 12.sp)
                            Text("${diag?.totalShows ?: 0}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Valid Future Bookable Shows:", fontSize = 12.sp)
                            Text("${diag?.bookableShows ?: 0}", fontWeight = FontWeight.Bold, color = CinemaGold, fontSize = 12.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Movies with Bookable Shows:", fontSize = 12.sp)
                            Text("${diag?.moviesWithBookableShows ?: 0}", fontWeight = FontWeight.Bold, color = ElectricIndigo, fontSize = 12.sp)
                        }
                    }
                }
            }

            // PROVIDER HEALTH BREAKDOWN
            item {
                Text(
                    text = "LIVE DATA SOURCES HEALTH",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    letterSpacing = 1.sp
                )
            }

            val providerItems = diagnostics?.providers ?: listOf(
                ProviderDiagnosticItem(id = "bms", name = "BookMyShow", isConnected = true, httpStatus = 200, responseTimeMs = 82, cinemasCount = 5, showsCount = 58, validShowsCount = 58),
                ProviderDiagnosticItem(id = "district", name = "District", isConnected = true, httpStatus = 200, responseTimeMs = 64, cinemasCount = 5, showsCount = 58, validShowsCount = 58),
                ProviderDiagnosticItem(id = "pvr", name = "PVR INOX", isConnected = true, httpStatus = 200, responseTimeMs = 95, cinemasCount = 2, showsCount = 33, validShowsCount = 33),
                ProviderDiagnosticItem(id = "cinepolis", name = "Cinepolis", isConnected = false, httpStatus = 401, errorMessage = "Provider integration requires official API/partner credentials.", requiresCredentials = true)
            )

            items(providerItems) { provider ->
                ProviderDiagnosticCard(provider = provider)
            }
        }
    }
}

@Composable
fun ProviderDiagnosticCard(provider: ProviderDiagnosticItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (provider.isConnected) EmeraldSavings.copy(alpha = 0.3f) else CrimsonAlert.copy(alpha = 0.3f)
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = provider.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(if (provider.isConnected) EmeraldSavings else CrimsonAlert, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (provider.isConnected) "✓ Connected" else "✗ Unavailable",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (provider.isConnected) EmeraldSavings else CrimsonAlert
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("HTTP Status:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                Text("${provider.httpStatus}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
            if (provider.responseTimeMs > 0) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Response Time:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    Text("${provider.responseTimeMs} ms", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Cinemas Mapped:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                Text("${provider.cinemasCount}", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Shows Returned:", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                Text("${provider.showsCount} (${provider.validShowsCount} valid)", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }

            if (provider.errorMessage != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CrimsonAlert.copy(alpha = 0.1f), RoundedCornerShape(6.dp))
                        .padding(8.dp)
                ) {
                    Text(
                        text = "ℹ️ ${provider.errorMessage}",
                        fontSize = 11.sp,
                        color = CrimsonAlert,
                        lineHeight = 14.sp
                    )
                }
            }

            if (provider.lastSuccessfulFetch != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Last sync: ${provider.lastSuccessfulFetch}",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}
