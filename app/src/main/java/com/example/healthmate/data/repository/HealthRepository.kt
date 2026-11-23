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

    suspend fun insert(record: HealthRecord) {
        dao.insert(record)
    }

    suspend fun update(record: HealthRecord) {
        dao.update(record)
    }

    suspend fun delete(record: HealthRecord) {
        dao.delete(record)
    }

    suspend fun seedIfEmpty() {
        if (dao.count() == 0) {
            dao.insertAll(DummyData.generate())
        }
    }
}