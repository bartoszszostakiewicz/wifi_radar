package com.wifi_radar.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [WiFiMeasurement::class], version = 3)
abstract class WiFiMeasurementsDatabase : RoomDatabase() {
    abstract fun wifiMeasurementDao(): WiFiMeasurementDao
}

object WiFiMeasurementsDb{
    private var db: WiFiMeasurementsDatabase? = null



    private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE measurements_table ADD COLUMN distance INTEGER NOT NULL DEFAULT 0")
        }
    }

    private val MIGRATION_2_3: Migration = object : Migration(2,3){
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE measurements_table ADD COLUMN measurementDate INTEGER NOT NULL DEFAULT 0")
        }
    }

    fun getInstance(context: Context): WiFiMeasurementsDatabase {

        if(db == null){
            db = Room.databaseBuilder(
                context,
                WiFiMeasurementsDatabase::class.java,
                "wifi_measurements_database"
            )
                .addMigrations(MIGRATION_1_2)
                .addMigrations(MIGRATION_2_3)
                .build()
        }

        return db!!
    }
}