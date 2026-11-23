package com.example.healthmate.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthmate.data.local.HealthRecord
import com.example.healthmate.data.repository.HealthRepository
import com.example.healthmate.util.DateUtils
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

const val STEPS_TARGET = 10000
const val CALORIES_TARGET = 500
const val WATER_TARGET = 2500

data class DashboardUiState(
    val date: String = "",
    val steps: Int = 0,
    val calories: Int = 0,
    val water: Int = 0
)

fun summarizeToday(records: List<HealthRecord>, today: String): DashboardUiState {
    val todays = records.filter { it.date == today }
    return DashboardUiState(
        date = today,
        steps = todays.sumOf { it.steps },
        calories = todays.sumOf { it.calories },
        water = todays.sumOf { it.water }
    )
}

class DashboardViewModel(repository: HealthRepository) : ViewModel() {

    val uiState: StateFlow<DashboardUiState> = repository.allRecords
        .map { summarizeToday(it, DateUtils.today()) }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            DashboardUiState(date = DateUtils.today())
        )
}