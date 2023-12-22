package com.wifi_radar.ui

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.wifi_radar.data.WiFiMeasurement
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale



enum class WifiScreens {
    MainScreen,
    HistoryScreen,
    RSSIChartScreen
}


@Composable
fun WifiDetailsScreen(navController: NavController, measurements: List<WiFiMeasurement>) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        itemsIndexed(measurements) { index, wifiMeasurement ->
            var isExpanded by rememberSaveable { mutableStateOf(index == 0) }

            Card(
                modifier = Modifier
                    .clickable { isExpanded = !isExpanded }
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .clip(RoundedCornerShape(20.dp)),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .background(Color(0xFF7EE9EC))
                ) {
                    Text(
                        text = wifiMeasurement.wifiName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.Black
                    )

                    Text(
                        text = "Connected to: ${wifiMeasurement.wifiName}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                    Button(
                        onClick = {
                        val route = "${WifiScreens.RSSIChartScreen.name}/${wifiMeasurement.wifiName.toString()}/${wifiMeasurement.frequency.toInt()}"
                        navController.navigate(route)
                    }) {
                        Text("RSSI Chart")
                    }


                    AnimatedVisibility(visible = isExpanded) {
                        Column {
                            Spacer(modifier = Modifier.height(8.dp))

                            WiFiDetailRow("SSID", wifiMeasurement.ssid)
                            WiFiDetailRow("RSSI", "${wifiMeasurement.rssi}dBm")
                            WiFiDetailRow("Link Speed", "${wifiMeasurement.linkSpeed}Mbps")
                            WiFiDetailRow("Frequency", "${wifiMeasurement.frequency}MHz")
                            WiFiDetailRow("Distance", "${wifiMeasurement.distance}m")
                            WiFiDetailRow("Date", formatTimestamp(wifiMeasurement.measurementDate))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WiFiDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$label: ",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            color = Color.DarkGray
        )
        Text(
            text = value,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
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


            Button(
                onClick = {
                    navController.navigate(WifiScreens.HistoryScreen.name)
                },

                ) {
                Text("Przejdź do historii")
            }



            Text(
                text = "Aktualne Pomiary",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp).align(Alignment.CenterHorizontally)
            )



            if (currentMeasurements != null) {
                if (currentMeasurements.isNotEmpty()) {
                    WifiDetailsScreen(navController ,currentMeasurements)
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
        startDestination = WifiScreens.MainScreen.name
    ) {
        composable(WifiScreens.MainScreen.name) { WiFiScreen(navController, viewModel) }
        composable(WifiScreens.HistoryScreen.name) { HistoryScreen(navController, viewModel) }
        composable(
            "${WifiScreens.RSSIChartScreen.name}/{measurementName}/{frequency}",
            arguments = listOf(
                navArgument("measurementName") { type = NavType.StringType },
                navArgument("frequency") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val measurementName = backStackEntry.arguments?.getString("measurementName")
            val frequency = backStackEntry.arguments?.getInt("frequency")
            RSSIChartScreen(navController, viewModel, measurementName, frequency)
        }
    }
}


