package com.wifi_radar.ui

import android.app.Application
import android.content.Context
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.wifi_radar.data.PropagationModel.Companion.freeSpaceModel
import com.wifi_radar.data.WiFiMeasurement
import com.wifi_radar.data.Repository
import com.wifi_radar.wifi.WiFi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.concurrent.fixedRateTimer

class WiFiViewModel(
   private val app: Application
) : AndroidViewModel(app) {

    private val repo = Repository(app.applicationContext)
    private val wifi = WiFi(app.applicationContext)


    private val _uiState = MutableStateFlow(WiFiUiState())
    val uiState: StateFlow<WiFiUiState> = _uiState.asStateFlow()

    fun getMeasurements(): Flow<List<WiFiMeasurement>> {
       return repo.getAllMeasurements()
    }

    init{
        updateWiFiInfo()
    }


    private fun updateWiFiInfo() {

       fixedRateTimer("timer", false, 0L, 50000) {


            _uiState.value = WiFiUiState()

           var deviceIsConnected : Boolean = false


           val wifiManager = app.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
           val wifiInfo = wifiManager.connectionInfo

           // Check if we are connected to a WiFi network
           if (wifiInfo != null && wifiInfo.networkId != -1) {
               val connectedSSID = wifiInfo.ssid.trim('"') // SSID of the connected network
               val linkSpeed = wifiInfo.linkSpeed // Link speed in Mbps

               // Now you can update your UI or data repository with this information
               Log.d("WiFiConnection", "Connected to: $connectedSSID with link speed: $linkSpeed Mbps")
                deviceIsConnected = true
           } else {
               Log.d("WiFiConnection", "Device is not connected to any WiFi network.")
                deviceIsConnected = false
           }


           val scanResults = wifi.startWifiScan(app.applicationContext)
            scanResults?.let {
                Log.d("WiFiScan4", "Skanowanie Wi-Fi")
                viewModelScope.launch {
                    val measurements = it.map { scanResult ->
                        WiFiMeasurement(
                            wifiName = scanResult.wifiSsid.toString(),
                            ssid = scanResult.wifiSsid.toString(),
                            rssi = scanResult.level.toDouble(),
                            linkSpeed = if (deviceIsConnected &&
                                wifiInfo.ssid.toString() == scanResult.wifiSsid.toString() &&
                                scanResult.frequency.toDouble() == wifiInfo.frequency.toDouble()
                                ) wifiInfo.linkSpeed.toDouble() else 0.0,
                            frequency = scanResult.frequency.toDouble(),
                            distance = freeSpaceModel(scanResult.level.toDouble(), scanResult.frequency.toDouble()),
                            measurementDate = System.currentTimeMillis(),
                        )
                    }
                    repo.insertAll(measurements)
                    // Update _uiState with new measurements
                    _uiState.value = _uiState.value.copy(measurements = measurements)
                }
            }
        }
    }


}

