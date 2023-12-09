package com.wifi_radar.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.wifi_radar.data.entities.WiFiMeasurementEntity

@Dao
interface WiFiMeasurementDao {
    @Insert
    suspend fun insert(measurement: WiFiMeasurementEntity)

    @Query("SELECT * FROM WiFiMeasurementEntity")
    suspend fun getAllMeasurements(): List<WiFiMeasurementEntity>
}
