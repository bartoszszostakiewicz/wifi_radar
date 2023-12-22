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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
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


    fun getRSSIWiFi(ssid : String, frequency : Double): Flow<List<Double>> {
        return repo.getAllMeasurements().map { measurements ->
            measurements.filter { it.ssid == ssid && it.frequency == frequency }.map { it.rssi }
        }
    }


    fun getRSSIConnectedWifi(): Flow<List<Double>> {
        return _uiState
            .map { uiState ->

                uiState.measurements.firstOrNull()?.let { measurement ->
                    if (measurement.linkSpeed != 0.0) measurement.ssid else ""
                } ?: ""
            }
            .flatMapLatest { connectedSSID ->
                if (connectedSSID.isNotEmpty()) {

                    repo.getAllMeasurements().map { measurements ->
                        measurements.filter { it.ssid == connectedSSID }.map { it.rssi }
                    }
                } else {

                    flowOf(emptyList())
                }
            }
    }


    init{
        updateWiFiInfo()
    }


    private fun updateWiFiInfo(period: Long = 50000) {

       fixedRateTimer("timer", false, 0L, period) {


            _uiState.value = WiFiUiState()

           var deviceIsConnected : Boolean = false


           val wifiManager = app.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

           //in feauture shall be used ConnectivityManager :)
           val wifiInfo = wifiManager.connectionInfo


           if (wifiInfo != null && wifiInfo.networkId != -1) {
               val connectedSSID = wifiInfo.ssid.trim('"')
               val linkSpeed = wifiInfo.linkSpeed

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
//                            wifiName = scanResult.wifiSsid.toString(),
                            wifiName = scanResult.SSID.toString(),
                            ssid = scanResult.SSID.toString(),
                            rssi = scanResult.level.toDouble(),
                            linkSpeed = if (wifiInfo.ssid.toString() == "\""+scanResult.SSID+"\"" &&
                                scanResult.frequency.toDouble() == wifiInfo.frequency.toDouble()) {
                                wifiInfo.linkSpeed.toDouble()
                            } else 0.0,

                            frequency = scanResult.frequency.toDouble(),
                            distance = freeSpaceModel(scanResult.level.toDouble(), scanResult.frequency.toDouble()),
                            measurementDate = System.currentTimeMillis(),
                        )
                    }
                        .sortedWith(compareByDescending { it.linkSpeed })

                    repo.insertAll(measurements)
                    _uiState.value = _uiState.value.copy(measurements = measurements)
                }

            }
        }
    }






}

