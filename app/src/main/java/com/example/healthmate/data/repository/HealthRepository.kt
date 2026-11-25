package com.example.healthmate.data.repository

import com.example.healthmate.data.local.DummyData
import com.example.healthmate.data.local.HealthRecord
import com.example.healthmate.data.local.HealthRecordDao
import kotlinx.coroutines.flow.Flow

class HealthRepository(private val dao: HealthRecordDao) {

    val allRecords: Flow<List<HealthRecord>> = dao.getAll()

    fun searchByDate(query: String): Flow<List<HealthRecord>> {
        return dao.searchByDate(query)
    }

    suspend fun getById(id: Int): HealthRecord? {
        return dao.getById(id)
    }

    // CREATE saves a new record
    suspend fun insert(record: HealthRecord) {
        dao.insert(record)
    }

    // CREATE or UPDATE
    suspend fun addToDate(record: HealthRecord) {
        val existing = dao.getByDate(record.date)
        if (existing == null) {
            dao.insert(record)
        } else {
            dao.update(
                existing.copy(
                    steps = existing.steps + record.steps,
                    calories = existing.calories + record.calories,
                    water = existing.water + record.water
                )
            )
        }
    }

    // UPDATE
    suspend fun update(record: HealthRecord) {
        dao.update(record)
    }

    // DELETE
    suspend fun delete(record: HealthRecord) {
        dao.delete(record)
    }

    suspend fun seedIfEmpty() {
        if (dao.count() == 0) {
            dao.insertAll(DummyData.generate())
        }
    }
}