package com.wifi_radar.ui

import com.wifi_radar.data.entities.WiFiMeasurementEntity

data class WiFiUiState(
    val measurements: List<WiFiMeasurementEntity> = emptyList(),
)


