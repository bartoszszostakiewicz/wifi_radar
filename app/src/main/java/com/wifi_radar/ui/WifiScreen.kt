package com.wifi_radar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentComposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wifi_radar.data.WiFiMeasurement
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


//ro

enum class WifiScreens {
    MainScreen,
    HistoryScreen
}


@Composable
fun WifiDetailsScreen(measurements: List<WiFiMeasurement>) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(measurements) { wifiMeasurement ->

            var isExpanded by rememberSaveable { mutableStateOf(false) }


            Card(
                modifier = Modifier
                    .clickable { isExpanded = !isExpanded }
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.White)
                ) {
                    Text(
                        text = wifiMeasurement.wifiName,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    if (isExpanded) {

                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Text(
                                "SSID: ",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(wifiMeasurement.ssid, style = MaterialTheme.typography.bodyLarge)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Text(
                                "RSSI: ",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                "${wifiMeasurement.rssi}dBm",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Text(
                                "Link Speed: ",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                "${wifiMeasurement.linkSpeed}Mbps",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Text(
                                "Frequency: ",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                "${wifiMeasurement.frequency}MHz",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Text(
                                "Distance: ",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                "${wifiMeasurement.distance}m",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Text(
                                "Date: ",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                // Załóżmy, że measurementDate to timestamp
                                formatTimestamp(wifiMeasurement.measurementDate),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun WiFiScreen(
    navController: NavController = rememberNavController(),
    viewModel: WiFiViewModel? = viewModel()
) {
    val currentMeasurements = viewModel?.uiState?.collectAsState()?.value?.measurements

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {

            Text(
                text = "WiFi Radar",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 16.dp),
            )

            Text(
                text = "Aktualne Pomiary",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Button(
                onClick = {
                    navController.navigate(WifiScreens.HistoryScreen.name)
                },

                ) {
                Text("Przejdź do historii")
            }

            if (currentMeasurements != null) {
                if (currentMeasurements.isNotEmpty()) {
                    WifiDetailsScreen(currentMeasurements)
                }
            }
        }
    }

}

fun formatTimestamp(measurementDate: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(measurementDate))
}


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val viewModel = viewModel<WiFiViewModel>()

    NavHost(
        navController = navController,
        startDestination = WifiScreens.MainScreen.name) {
        composable(WifiScreens.MainScreen.name) { WiFiScreen(navController,viewModel) }
        composable(WifiScreens.HistoryScreen.name) { HistoryScreen(navController,viewModel) }
    }
}


