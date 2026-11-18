package com.example.healthmate.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthmate.data.local.DEFAULT_CALORIES_GOAL
import com.example.healthmate.data.local.DEFAULT_STEPS_GOAL
import com.example.healthmate.data.local.DEFAULT_WATER_GOAL
import com.example.healthmate.data.local.GoalPreferences
import com.example.healthmate.data.local.HealthRecord
import com.example.healthmate.data.repository.HealthRepository
import com.example.healthmate.util.DateUtils
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

// Everything the Dashboard screen needs to display
data class DashboardUiState(
    val date: String = "",                          // today's date text
    val steps: Int = 0,                             // total steps today
    val calories: Int = 0,                          // total calories burned today (kcal)
    val water: Int = 0,                             // total water drunk today (ml)
    val stepsGoal: Int = DEFAULT_STEPS_GOAL,        // the user's daily targets
    val caloriesGoal: Int = DEFAULT_CALORIES_GOAL,
    val waterGoal: Int = DEFAULT_WATER_GOAL
)

// Pure function: adds up all records that belong to "today".
// Kept separate from the ViewModel so it is easy to unit test.
fun List<HealthRecord>.toDashboardState(today: String): DashboardUiState {
    val todays = filter { it.date == today }      // keep only today's records
    return DashboardUiState(
        date = today,
        steps = todays.sumOf { it.steps },        // add up each field
        calories = todays.sumOf { it.calories },
        water = todays.sumOf { it.water }
    )
}

class DashboardViewModel(
    repository: HealthRepository,
    goalPreferences: GoalPreferences
) : ViewModel() {

    // Recalculates whenever the records OR the goals change
    val uiState: StateFlow<DashboardUiState> = combine(
        repository.allRecords,
        goalPreferences.goals
    ) { records, goals ->
        records.toDashboardState(DateUtils.today()).copy(
            stepsGoal = goals.steps,                // add the saved targets to the totals
            caloriesGoal = goals.calories,
            waterGoal = goals.water
        )
    }.stateIn(
        scope = viewModelScope,                           // lives as long as the ViewModel
        started = SharingStarted.WhileSubscribed(5_000),  // stops 5s after the screen goes away
        initialValue = DashboardUiState(date = DateUtils.today()) // shown before data loads
    )
}