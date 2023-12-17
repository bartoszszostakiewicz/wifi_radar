package com.wifi_radar.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.wifi_radar.data.WiFiMeasurement
import kotlinx.coroutines.flow.Flow

@Dao
interface WiFiMeasurementDao {

    @Insert
    suspend fun insertAll(measurements: List<WiFiMeasurement>)

    @Delete
    suspend fun delete(measurement: List<WiFiMeasurement>)

    @Update
    suspend fun update(measurement: WiFiMeasurement)

    @Query("SELECT * FROM measurements_table")
    fun getAllMeasurements(): Flow<List<WiFiMeasurement>>

    @Query("DELETE FROM measurements_table")
    suspend fun dropDatabase()

}
