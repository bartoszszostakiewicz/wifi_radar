package com.wifi_radar.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "measurements_table")
data class WiFiMeasurement(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var wifiName: String = "",
    var ssid: String = "",
    var rssi: Double = 0.00,
    var linkSpeed: Double = 0.00,
    var frequency: Double = 0.00,
    var distance: Int = 0,
    var measurementDate: Long = 0,
){

}
