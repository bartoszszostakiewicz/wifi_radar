package com.wifi_radar.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import kotlin.math.abs


@Composable
fun RSSIChartScreen(
    navController: NavController = rememberNavController(),
    viewModel: WiFiViewModel = viewModel(),
    measurementName: String?,
    frequency: Int?
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { navController.popBackStack() }) {
                    Text(text = "Wróć")
                }
                Text(text = "RSSI sygnału", style = MaterialTheme.typography.headlineSmall)
            }

            Spacer(modifier = Modifier.height(16.dp))


            //val rssi = viewModel.getRSSIConnectedWifi().collectAsState(initial = emptyList()).value

            val rssi = viewModel.getRSSIWiFi(measurementName!!, frequency!!.toDouble()).collectAsState(initial = emptyList()).value

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .height(200.dp)
                    .border(1.dp, Color.Gray)
                    .padding(16.dp)
            ) {
                DrawRSSIChart(rssi)
            }
        }
    }
}


@Composable
fun DrawRSSIChart(rssi: List<Double>) {
    if (rssi.isEmpty()) {
        Text("Brak danych RSSI do wyświetlenia")
    } else {
        val lineChartData = rssi.mapIndexed { index, value ->
            Point(index.toFloat(), value.toFloat())
        }

        val xAxisData = AxisData.Builder()
            .axisStepSize(7.dp)
            .backgroundColor(Color.Transparent)
            .steps(lineChartData.size - 1)
            .labelData { i ->
                if (i == 5) {
                    "Najstarsze"
                } else if (i == 25) {
                    "Najnowsze"
                } else {
                    ""
                }
            }
            .build()

        val maxYValue = lineChartData.maxByOrNull { it.y }?.y ?: 0f
        val minYValue = lineChartData.minByOrNull { it.y }?.y ?: 0f

        val YAxisSteps = 20

        val yAxisData = AxisData.Builder()
            .steps(YAxisSteps)
            .axisStepSize(10.dp)
            .backgroundColor(Color.Transparent)
            .labelData { i ->
                val yScale = (abs(minYValue)-abs(maxYValue)) / YAxisSteps
                val scaledValue = (minYValue + (i * yScale)).toDouble()
                scaledValue.toString()
            }
            .build()

        val chartData = LineChartData(
            linePlotData = LinePlotData(
                lines = listOf(
                    Line(
                        dataPoints = lineChartData,
                        LineStyle(
                            color = MaterialTheme.colorScheme.tertiary
                        ),
                        IntersectionPoint(
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            radius = 3.dp
                        ),
                    )
                )
            ),
            backgroundColor = MaterialTheme.colorScheme.background,
            xAxisData = xAxisData,
            yAxisData = yAxisData,
            gridLines = GridLines(color = MaterialTheme.colorScheme.tertiaryContainer),
        )

        LineChart(
            lineChartData = chartData,
            modifier = Modifier
                .fillMaxSize()
                .fillMaxWidth()
                .height(500.dp)
        )
    }
}
