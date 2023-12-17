package com.wifi_radar.ui

import com.wifi_radar.data.WiFiMeasurement


data class WiFiUiState(
    val measurements: List<WiFiMeasurement> = emptyList(),
)


