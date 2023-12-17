package com.wifi_radar.data

import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow

class PropagationModel {
    companion object {

        fun freeSpaceModel(rssi: Double, frequency: Double): Int {
            val d = ((27.55 - (20 * log10(frequency)) + abs(rssi)) / 20.0)
            return (10.0.pow(d)).toInt()
        }
    }
}
