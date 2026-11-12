package com.example.healthmate.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

// @Dao = Data Access Object: the functions used to talk to the table
@Dao
interface HealthRecordDao {

    // CREATE: add a new record (suspend = runs off the main thread)
    @Insert
    suspend fun insert(record: HealthRecord)

    // UPDATE: change an existing record (matched by its id)
    @Update
    suspend fun update(record: HealthRecord)

    // DELETE: remove a record
    @Delete
    suspend fun delete(record: HealthRecord)

    // READ: all records, newest date first. Flow re-emits when data changes,
    // so the screen updates automatically
    @Query("SELECT * FROM health_records ORDER BY date DESC")
    fun getAll(): Flow<List<HealthRecord>>

    // SEARCH: records whose date contains the typed text (e.g. "2026-11")
    @Query("SELECT * FROM health_records WHERE date LIKE '%' || :text || '%' ORDER BY date DESC")
    fun searchByDate(text: String): Flow<List<HealthRecord>>

    // READ ONE: used to fill the edit form
    @Query("SELECT * FROM health_records WHERE id = :id")
    suspend fun getById(id: Int): HealthRecord?

    // Used to decide whether to insert the dummy data
    @Query("SELECT COUNT(*) FROM health_records")
    suspend fun count(): Int
}

