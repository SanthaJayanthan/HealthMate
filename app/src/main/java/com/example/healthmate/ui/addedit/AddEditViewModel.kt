package com.example.healthmate.ui.addedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthmate.data.local.HealthRecord
import com.example.healthmate.data.repository.HealthRepository
import com.example.healthmate.util.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddEditUiState(
    val id: Int = 0,
    val date: String = DateUtils.today(),
    val steps: String = "",
    val calories: String = "",
    val water: String = "",
    val dateError: String? = null,
    val stepsError: String? = null,
    val caloriesError: String? = null,
    val waterError: String? = null,
    val saved: Boolean = false
)

class AddEditViewModel(
    private val repository: HealthRepository,
    private val recordId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditUiState())
    val uiState: StateFlow<AddEditUiState> = _uiState.asStateFlow()

    val isEditing: Boolean = recordId > 0

    init {
        if (isEditing) {
            viewModelScope.launch {
                repository.getById(recordId)?.let { record ->
                    _uiState.update {
                        it.copy(
                            id = record.id,
                            date = record.date,
                            steps = record.steps.toString(),
                            calories = record.calories.toString(),
                            water = record.water.toString()
                        )
                    }
                }
            }
        }
    }

    fun onDateChange(value: String) {
        _uiState.update { it.copy(date = value, dateError = null) }
    }

    fun onStepsChange(value: String) {
        _uiState.update { it.copy(steps = value, stepsError = null) }
    }

    fun onCaloriesChange(value: String) {
        _uiState.update { it.copy(calories = value, caloriesError = null) }
    }

    fun onWaterChange(value: String) {
        _uiState.update { it.copy(water = value, waterError = null) }
    }

    fun save() {
        val current = _uiState.value
        val dateError = FormValidator.validateDate(current.date)
        val stepsError = FormValidator.validateSteps(current.steps)
        val caloriesError = FormValidator.validateCalories(current.calories)
        val waterError = FormValidator.validateWater(current.water)

        if (dateError != null || stepsError != null || caloriesError != null || waterError != null) {
            _uiState.update {
                it.copy(
                    dateError = dateError,
                    stepsError = stepsError,
                    caloriesError = caloriesError,
                    waterError = waterError
                )
            }
            return
        }

        val record = HealthRecord(
            id = current.id,
            date = current.date.trim(),
            steps = current.steps.trim().toInt(),
            calories = current.calories.trim().toInt(),
            water = current.water.trim().toInt()
        )

        viewModelScope.launch {
            if (current.id == 0) repository.insert(record) else repository.update(record)
            _uiState.update { it.copy(saved = true) }
        }
    }
}