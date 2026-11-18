package com.example.healthmate.ui.goals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.healthmate.data.local.GoalPreferences
import com.example.healthmate.data.local.Goals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

// Everything the Goals form shows. Fields are text because the user types them.
data class GoalsUiState(
    val steps: String = "",
    val calories: String = "",
    val water: String = "",
    val stepsError: String? = null,       // error messages (null = no error)
    val caloriesError: String? = null,
    val waterError: String? = null,
    val saved: Boolean = false            // becomes true after a successful save
)

class GoalsViewModel(private val preferences: GoalPreferences) : ViewModel() {

    // The form starts filled with the goals that are currently saved
    private val _uiState = MutableStateFlow(
        preferences.goals.value.let {
            GoalsUiState(
                steps = it.steps.toString(),
                calories = it.calories.toString(),
                water = it.water.toString()
            )
        }
    )
    val uiState: StateFlow<GoalsUiState> = _uiState

    // Each change function updates one field and clears that field's old error
    fun onStepsChange(value: String) = _uiState.update { it.copy(steps = value, stepsError = null) }
    fun onCaloriesChange(value: String) = _uiState.update { it.copy(calories = value, caloriesError = null) }
    fun onWaterChange(value: String) = _uiState.update { it.copy(water = value, waterError = null) }

    // Validates everything; saves only if every field is valid
    fun save() {
        val s = _uiState.value
        val stepsError = GoalValidator.validate(s.steps, "Steps", GoalValidator.MAX_STEPS)
        val caloriesError = GoalValidator.validate(s.calories, "Calories", GoalValidator.MAX_CALORIES)
        val waterError = GoalValidator.validate(s.water, "Water", GoalValidator.MAX_WATER)

        // If any field is invalid, show the messages and stop (nothing is saved)
        if (stepsError != null || caloriesError != null || waterError != null) {
            _uiState.update {
                it.copy(stepsError = stepsError, caloriesError = caloriesError, waterError = waterError)
            }
            return
        }

        // All valid: store the new goals and tell the screen to close
        preferences.save(
            Goals(
                steps = s.steps.trim().toInt(),
                calories = s.calories.trim().toInt(),
                water = s.water.trim().toInt()
            )
        )
        _uiState.update { it.copy(saved = true) }
    }
}

// Small factory so Android can create the ViewModel with its preferences
class GoalsViewModelFactory(private val preferences: GoalPreferences) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = GoalsViewModel(preferences) as T
}

