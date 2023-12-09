package com.wifi_radar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wifi_radar.data.WiFiMeasurement
import com.wifi_radar.data.dao.WiFiMeasurementDao
import com.wifi_radar.data.repository.WiFiRepository
import com.wifi_radar.ui.WiFiUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class WiFiViewModel @Inject constructor(
    private val repository: WiFiRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WiFiUiState())
    val uiState: StateFlow<WiFiUiState> = _uiState.asStateFlow()



    init {

        loadMeasurements()
    }

    fun saveMeasurementToRoomDB(measurement: WiFiMeasurement) {
        viewModelScope.launch {
            try {
                repository.saveMeasurement(measurement)
                // Aktualizacja stanu UI, np. z potwierdzeniem zapisu
            } catch (e: Exception) {
                // Obsługa błędów
            }
        }
    }

    fun loadMeasurements() {
        viewModelScope.launch {
            try {
                repository.addTestData()
                val measurements = repository.getMeasurements()
                _uiState.value = _uiState.value.copy(measurements = measurements)
            } catch (e: Exception) {
                // Obsługa błędów
            }
        }
    }

    // Dodatkowe funkcje, np. do skanowania Wi-Fi, aktualizacji UI itp.
}