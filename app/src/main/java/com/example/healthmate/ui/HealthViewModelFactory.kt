package com.example.healthmate.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.healthmate.data.repository.HealthRepository
import com.example.healthmate.ui.addedit.AddEditViewModel
import com.example.healthmate.ui.dashboard.DashboardViewModel
import com.example.healthmate.ui.records.RecordListViewModel

class HealthViewModelFactory(
    private val repository: HealthRepository,
    private val recordId: Int = 0
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DashboardViewModel::class.java) ->
                DashboardViewModel(repository) as T
            modelClass.isAssignableFrom(RecordListViewModel::class.java) ->
                RecordListViewModel(repository) as T
            modelClass.isAssignableFrom(AddEditViewModel::class.java) ->
                AddEditViewModel(repository, recordId) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}