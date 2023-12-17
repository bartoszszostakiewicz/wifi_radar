package com.wifi_radar.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController


@Composable
fun HistoryScreen(navController: NavController = rememberNavController(), viewModel: WiFiViewModel = viewModel()) {
    val historyMeasurements = viewModel?.getMeasurements()?.collectAsState(initial = emptyList())?.value


    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navController.navigate(WifiScreens.MainScreen.name) },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("Powrót do głównego ekranu")
            }
            Text(
                text = "Historia Pomiarów",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            if (!historyMeasurements.isNullOrEmpty()) {
                WifiDetailsScreen(historyMeasurements)
            }
        }
    }
}

