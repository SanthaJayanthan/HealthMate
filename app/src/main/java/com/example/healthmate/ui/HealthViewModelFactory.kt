package com.example.healthmate.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.healthmate.data.repository.HealthRepository
import com.example.healthmate.ui.dashboard.DashboardViewModel
import com.example.healthmate.ui.records.RecordListViewModel

// Our ViewModels need the repository, so Android can't create them on its own.
// This factory tells Android how to build each one.
class HealthViewModelFactory(private val repository: HealthRepository) : ViewModelProvider.Factory {

    // The cast is safe because we check the class first
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            DashboardViewModel::class.java -> DashboardViewModel(repository) as T
            RecordListViewModel::class.java -> RecordListViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }
    }
}

