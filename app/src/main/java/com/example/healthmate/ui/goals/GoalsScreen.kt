package com.example.healthmate.ui.goals

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

// Goals form: lets the user set their own daily targets
@Composable
fun GoalsScreen(
    viewModel: GoalsViewModel,
    onBack: () -> Unit,          // called when the back arrow is tapped
    onSaved: () -> Unit,         // called after a successful save
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    // When the ViewModel reports a successful save, tell the caller
    LaunchedEffect(state.saved) {
        if (state.saved) onSaved()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Back button and title
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text("Daily goals", style = MaterialTheme.typography.headlineSmall)
        }

        Text("Set the targets the Dashboard progress bars aim for.", style = MaterialTheme.typography.bodyMedium)

        // The three goal fields, each showing its own error message
        GoalField(state.steps, viewModel::onStepsChange, "Steps goal", state.stepsError)
        GoalField(state.calories, viewModel::onCaloriesChange, "Calories goal (kcal)", state.caloriesError)
        GoalField(state.water, viewModel::onWaterChange, "Water goal (ml)", state.waterError)

        // Save runs the validation inside the ViewModel
        Button(onClick = viewModel::save, modifier = Modifier.fillMaxWidth()) {
            Text("Save goals")
        }
    }
}

// One number field that turns red and shows a message when there is an error
@Composable
private fun GoalField(value: String, onChange: (String) -> Unit, label: String, error: String?) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        isError = error != null,                              // red outline when invalid
        supportingText = { if (error != null) Text(error) },  // message under the field
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // number pad
        modifier = Modifier.fillMaxWidth()
    )
}

