package com.example.healthmate.ui.addedit

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AddEditScreen(
    viewModel: AddEditViewModel,
    onSaved: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.saved) {
        if (state.saved) onSaved(viewModel.isEditing)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = if (viewModel.isEditing) "Edit Record" else "Add Record",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        FormField(
            value = state.date,
            onChange = viewModel::onDateChange,
            label = "Date (yyyy-MM-dd)",
            error = state.dateError,
            keyboardType = KeyboardType.Text
        )
        FormField(
            value = state.steps,
            onChange = viewModel::onStepsChange,
            label = "Steps",
            error = state.stepsError,
            keyboardType = KeyboardType.Number
        )
        FormField(
            value = state.calories,
            onChange = viewModel::onCaloriesChange,
            label = "Calories burned (kcal)",
            error = state.caloriesError,
            keyboardType = KeyboardType.Number
        )
        FormField(
            value = state.water,
            onChange = viewModel::onWaterChange,
            label = "Water intake (ml)",
            error = state.waterError,
            keyboardType = KeyboardType.Number
        )

        Button(
            onClick = viewModel::save,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (viewModel.isEditing) "Update" else "Save")
        }
    }
}

@Composable
private fun FormField(
    value: String,
    onChange: (String) -> Unit,
    label: String,
    error: String?,
    keyboardType: KeyboardType
) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        isError = error != null,
        supportingText = { if (error != null) Text(error) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}