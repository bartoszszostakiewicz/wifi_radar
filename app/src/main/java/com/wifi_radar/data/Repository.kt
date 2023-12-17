package com.wifi_radar.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(context: Context) : WiFiMeasurementDao {

    private val dao = WiFiMeasurementsDb.getInstance(context).wifiMeasurementDao()


    override suspend fun insertAll(measurements: List<WiFiMeasurement>) =
        withContext(Dispatchers.IO) {
            dao.insertAll(measurements)
        }

    override suspend fun delete(measurement: List<WiFiMeasurement>) = withContext(Dispatchers.IO) {
        dao.delete(measurement)
    }

    override suspend fun update(measurement: WiFiMeasurement) = withContext(Dispatchers.IO) {
        dao.update(measurement)
    }

    override fun getAllMeasurements() = dao.getAllMeasurements()

    override suspend fun dropDatabase() = withContext(Dispatchers.IO) {
        dao.dropDatabase()
    }
}