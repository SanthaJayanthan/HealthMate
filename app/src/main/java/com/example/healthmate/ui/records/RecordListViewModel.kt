package com.example.healthmate.ui.records

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthmate.data.local.HealthRecord
import com.example.healthmate.data.repository.HealthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// OptIn is needed because flatMapLatest is still marked experimental
@OptIn(ExperimentalCoroutinesApi::class)
class RecordListViewModel(private val repository: HealthRepository) : ViewModel() {

    // Text currently typed in the search box (starts empty)
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    // The list shown on screen: all records if the box is empty,
    // otherwise only records whose date matches the typed text
    val records: StateFlow<List<HealthRecord>> = _searchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) repository.allRecords
            else repository.search(query.trim())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    // Called by the search box every time the text changes
    fun onSearchChange(text: String) {
        _searchQuery.value = text
    }

    // DELETE: runs in the background so the screen never freezes
    fun deleteRecord(record: HealthRecord) {
        viewModelScope.launch { repository.delete(record) }
    }
}

