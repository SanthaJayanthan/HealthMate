package com.example.healthmate.ui.addedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.healthmate.data.local.HealthRecord
import com.example.healthmate.data.repository.HealthRepository
import com.example.healthmate.util.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Everything the Add/Edit form shows. Fields are text because the user types them.
data class AddEditUiState(
    val date: String = DateUtils.today(),   // new records start with today's date
    val steps: String = "",
    val calories: String = "",
    val water: String = "",
    val dateError: String? = null,          // error messages (null = no error)
    val stepsError: String? = null,
    val caloriesError: String? = null,
    val waterError: String? = null,
    val isEditing: Boolean = false,         // true when changing an existing record
    val saved: Boolean = false              // becomes true after a successful save
)

// recordId = null means "add new"; a number means "edit that record"
class AddEditViewModel(
    private val repository: HealthRepository,
    private val recordId: Int?
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditUiState(isEditing = recordId != null))
    val uiState: StateFlow<AddEditUiState> = _uiState

    // When editing, load the existing record into the form straight away
    init {
        if (recordId != null) {
            viewModelScope.launch {
                repository.getById(recordId)?.let { record ->
                    _uiState.update {
                        it.copy(
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

    // Each change function updates one field and clears that field's old error
    fun onDateChange(value: String) = _uiState.update { it.copy(date = value, dateError = null) }
    fun onStepsChange(value: String) = _uiState.update { it.copy(steps = value, stepsError = null) }
    fun onCaloriesChange(value: String) = _uiState.update { it.copy(calories = value, caloriesError = null) }
    fun onWaterChange(value: String) = _uiState.update { it.copy(water = value, waterError = null) }

    // Validates everything; saves only if every field is valid
    fun save() {
        val s = _uiState.value
        val dateError = FormValidator.validateDate(s.date)
        val stepsError = FormValidator.validateNumber(s.steps, "Steps", FormValidator.MAX_STEPS)
        val caloriesError = FormValidator.validateNumber(s.calories, "Calories", FormValidator.MAX_CALORIES)
        val waterError = FormValidator.validateNumber(s.water, "Water", FormValidator.MAX_WATER)

        // If any field is invalid, show the messages and stop (nothing is saved)
        if (listOf(dateError, stepsError, caloriesError, waterError).any { it != null }) {
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

        // All valid: build the record (id 0 = new, otherwise keep the old id)
        val record = HealthRecord(
            id = recordId ?: 0,
            date = s.date,
            steps = s.steps.trim().toInt(),
            calories = s.calories.trim().toInt(),
            water = s.water.trim().toInt()
        )
        viewModelScope.launch {
            if (recordId == null) repository.add(record) else repository.update(record)
            _uiState.update { it.copy(saved = true) }   // tells the screen to close
        }
    }
}

// This ViewModel needs the record id as well, so it has its own small factory
class AddEditViewModelFactory(
    private val repository: HealthRepository,
    private val recordId: Int?
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        AddEditViewModel(repository, recordId) as T
}