package com.wifi_radar.wifi

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class WiFi(context: Context) {

    private val c = context
    private val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

    fun startWifiScan(context: Context): MutableList<ScanResult>? {
        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //depraceted method startScan is used because there is no other way scan,
            val success = wifiManager.startScan()
            if (!success) {
                Log.d("WiFiScan4", "Błąd rozpoczęcia skanowania Wi-Fi")
                return null
            } else {
                Log.d("WiFiScan4", "Skanowanie Wi-Fi")

                return getScanResults()
            }
        } else {
            Log.d("WiFiScan4", "Brak uprawnień do lokalizacji")
            return null
        }

    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun getScanResults(): MutableList<ScanResult>? {
        val results = if (ActivityCompat.checkSelfPermission(
                this.c,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        } else {

            wifiManager.scanResults
        }
        for (scanResult in results) {
            Log.d(
                "WiFiScan4",
                "SSID: ${scanResult.SSID}, Signal Strength: ${scanResult.level} "

            )
        }

        return results
    }

}
