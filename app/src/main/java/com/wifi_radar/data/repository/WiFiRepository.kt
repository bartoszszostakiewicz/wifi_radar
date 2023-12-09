package com.wifi_radar.data.repository

import com.wifi_radar.data.WiFiMeasurement
import com.wifi_radar.data.dao.WiFiMeasurementDao
import com.wifi_radar.data.entities.WiFiMeasurementEntity
import javax.inject.Singleton

class WiFiRepository(private val dao: WiFiMeasurementDao) {


    suspend fun saveMeasurement(measurement: WiFiMeasurement) {
        dao.insert(measurement.toEntity())
    }

    suspend fun getMeasurements(): List<WiFiMeasurementEntity> {
        return dao.getAllMeasurements()
    }

    suspend fun addTestData() {

        val testMeasurements = listOf(
            WiFiMeasurement("ExampleWiFi", "SSID123", -50.0, 54.0, 2400.0),
            WiFiMeasurement("HomeNetwork", "SSID234", -60.0, 48.0, 2450.0),
            WiFiMeasurement("CoffeeShopWiFi", "SSID345", -70.0, 36.0, 2420.0),
            WiFiMeasurement("OfficeNet", "SSID456", -55.0, 72.0, 5200.0),
            WiFiMeasurement("PublicHotspot", "SSID567", -80.0, 24.0, 2370.0),
            WiFiMeasurement("GuestNetwork", "SSID678", -65.0, 60.0, 5300.0),
        )


        testMeasurements.forEach { measurement ->
            saveMeasurement(measurement)
        }
    }
}


