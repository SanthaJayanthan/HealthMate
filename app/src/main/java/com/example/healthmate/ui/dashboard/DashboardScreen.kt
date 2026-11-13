package com.example.healthmate.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.healthmate.ui.theme.CaloriesColor
import com.example.healthmate.ui.theme.StepsColor
import com.example.healthmate.ui.theme.WaterColor

// Daily targets used for the progress bars
private const val STEPS_GOAL = 10_000
private const val CALORIES_GOAL = 500
private const val WATER_GOAL = 2_500

// Dashboard: shows today's totals as three colour-coded cards
@Composable
fun DashboardScreen(viewModel: DashboardViewModel, modifier: Modifier = Modifier) {
    // collectAsState turns the ViewModel's flow into state; the screen redraws when it changes
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),   // scrolls on small screens
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen title and today's date
        Text("HealthMate", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Today: ${state.date}", style = MaterialTheme.typography.titleMedium)

        // One card per metric
        MetricCard("Steps", state.steps, STEPS_GOAL, "steps", Icons.AutoMirrored.Filled.DirectionsWalk, StepsColor)
        MetricCard("Calories burned", state.calories, CALORIES_GOAL, "kcal", Icons.Filled.LocalFireDepartment, CaloriesColor)
        MetricCard("Water intake", state.water, WATER_GOAL, "ml", Icons.Filled.WaterDrop, WaterColor)
    }
}

// A card with an icon, the value, and a progress bar towards the daily goal
@Composable
private fun MetricCard(label: String, value: Int, goal: Int, unit: String, icon: ImageVector, color: Color) {
    // Progress between 0 and 1 (capped so the bar never overflows)
    val progress = (value.toFloat() / goal).coerceIn(0f, 1f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.12f)) // light tint of the metric colour
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Icon and label row
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(32.dp))
                Spacer(Modifier.width(12.dp))
                Text(label, style = MaterialTheme.typography.titleMedium)
            }
            Spacer(Modifier.height(8.dp))
            // The big number
            Text("$value $unit", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = color)
            Spacer(Modifier.height(8.dp))
            // Progress towards the goal
            LinearProgressIndicator(progress = { progress }, modifier = Modifier.fillMaxWidth(), color = color)
            Spacer(Modifier.height(4.dp))
            Text("Goal: $goal $unit", style = MaterialTheme.typography.bodySmall)
        }
    }
}