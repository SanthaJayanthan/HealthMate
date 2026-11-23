package com.example.healthmate.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HealthRecordDao {

    @Query("SELECT * FROM health_records ORDER BY date DESC, id DESC")
    fun getAll(): Flow<List<HealthRecord>>

    @Query("SELECT * FROM health_records WHERE date LIKE '%' || :query || '%' ORDER BY date DESC, id DESC")
    fun searchByDate(query: String): Flow<List<HealthRecord>>

    @Query("SELECT * FROM health_records WHERE id = :id")
    suspend fun getById(id: Int): HealthRecord?

    @Query("SELECT COUNT(*) FROM health_records")
    suspend fun count(): Int

    @Insert
    suspend fun insert(record: HealthRecord)

    @Insert
    suspend fun insertAll(records: List<HealthRecord>)

    @Update
    suspend fun update(record: HealthRecord)

    @Delete
    suspend fun delete(record: HealthRecord)
}