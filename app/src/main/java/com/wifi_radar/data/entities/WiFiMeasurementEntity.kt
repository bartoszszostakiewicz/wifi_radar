package com.wifi_radar.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WiFiMeasurementEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val wifiName: String,
    val ssid: String,
    val rssi: Double,
    val linkSpeed: Double,
    val frequency: Double,

)