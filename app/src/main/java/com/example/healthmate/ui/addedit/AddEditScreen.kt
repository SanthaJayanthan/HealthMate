package com.example.healthmate.ui.addedit

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

// Add/Edit form. The same screen is used for both; the ViewModel knows which.
@Composable
fun AddEditScreen(
    viewModel: AddEditViewModel,
    onBack: () -> Unit,                    // called when the back arrow is tapped
    onSaved: (wasEditing: Boolean) -> Unit, // called after a successful save (true = edit, false = add)
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    // When the ViewModel reports a successful save, tell the caller what kind of save it was
    LaunchedEffect(state.saved) {
        if (state.saved) onSaved(state.isEditing)
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
            Text(
                text = if (state.isEditing) "Edit record" else "Add record",
                style = MaterialTheme.typography.headlineSmall
            )
        }

        // The four input fields, each showing its own error message
        FormField(state.date, viewModel::onDateChange, "Date (yyyy-MM-dd)", state.dateError, KeyboardType.Text)
        FormField(state.steps, viewModel::onStepsChange, "Steps", state.stepsError, KeyboardType.Number)
        FormField(state.calories, viewModel::onCaloriesChange, "Calories", state.caloriesError, KeyboardType.Number)
        FormField(state.water, viewModel::onWaterChange, "Water", state.waterError, KeyboardType.Number)

        // Save runs the validation inside the ViewModel
        Button(onClick = viewModel::save, modifier = Modifier.fillMaxWidth()) {
            Text(if (state.isEditing) "Update" else "Save")
        }
    }
}

// One text field that turns red and shows a message when there is an error
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
        isError = error != null,                              // red outline when invalid
        supportingText = { if (error != null) Text(error) },  // message under the field
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType), // number pad for numbers
        modifier = Modifier.fillMaxWidth()
    )
}