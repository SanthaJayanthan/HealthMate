package com.example.healthmate.data.repository

import com.example.healthmate.data.local.DummyData
import com.example.healthmate.data.local.HealthRecord
import com.example.healthmate.data.local.HealthRecordDao
import kotlinx.coroutines.flow.Flow

// The only class the ViewModels talk to; it hides the DAO from the UI layer
class HealthRepository(private val dao: HealthRecordDao) {

    // All records, newest first; updates automatically when data changes
    val allRecords: Flow<List<HealthRecord>> = dao.getAll()

    // Records whose date contains the typed text
    fun search(text: String): Flow<List<HealthRecord>> = dao.searchByDate(text)

    // One record by id (used to fill the edit form)
    suspend fun getById(id: Int): HealthRecord? = dao.getById(id)

    // CREATE
    suspend fun add(record: HealthRecord) = dao.insert(record)

    // UPDATE
    suspend fun update(record: HealthRecord) = dao.update(record)

    // DELETE
    suspend fun delete(record: HealthRecord) = dao.delete(record)

    // Inserts the dummy records only if the table is empty (so no duplicates)
    suspend fun seedIfEmpty() {
        if (dao.count() == 0) {
            DummyData.generate().forEach { dao.insert(it) }
        }
    }
}

