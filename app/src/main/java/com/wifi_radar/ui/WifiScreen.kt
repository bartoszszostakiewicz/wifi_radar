package com.wifi_radar.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wifi_radar.WiFiViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import com.wifi_radar.data.entities.WiFiMeasurementEntity


@Composable
fun WifiDetailsScreen(measurements: List<WiFiMeasurementEntity>) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(measurements) { wifiMeasurement -> // Tutaj 'items' przyjmuje listę 'measurements'
            Column {
                Text("WiFi name: ${wifiMeasurement.wifiName}")
                Text("SSID: ${wifiMeasurement.ssid}")
                Text("RSSI: ${wifiMeasurement.rssi}dBm")
                Text("Link Speed: ${wifiMeasurement.linkSpeed}Mbps")
                Text("Frequency: ${wifiMeasurement.frequency}MHz")
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}



@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun WiFiScreen(viewModel: WiFiViewModel = viewModel()) {

    val uiState = viewModel.uiState.collectAsState().value

    WifiDetailsScreen(uiState.measurements)
}