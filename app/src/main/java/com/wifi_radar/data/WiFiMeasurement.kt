package com.wifi_radar.data

import com.wifi_radar.data.entities.WiFiMeasurementEntity

data class WiFiMeasurement(
    val wifiName: String = "",
    val ssid: String = "",
    val rssi: Double = 0.00,
    val linkSpeed: Double = 0.00,
    val frequency: Double = 0.00,
){
    fun toEntity(): WiFiMeasurementEntity{
        return WiFiMeasurementEntity(
            wifiName = wifiName,
            ssid = ssid,
            rssi = rssi,
            linkSpeed = linkSpeed,
            frequency = frequency,
        )
    }
}
