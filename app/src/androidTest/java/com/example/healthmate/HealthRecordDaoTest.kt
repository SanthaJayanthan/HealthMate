package com.example.healthmate

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.healthmate.data.local.AppDatabase
import com.example.healthmate.data.local.HealthRecord
import com.example.healthmate.data.local.HealthRecordDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HealthRecordDaoTest {
    private lateinit var db: AppDatabase
    private lateinit var dao: HealthRecordDao

    // Runs before each test: a fresh in-memory database (nothing saved to disk)
    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dao = db.healthRecordDao()
    }

    // Runs after each test: close the database
    @After
    fun tearDown() = db.close()

    // TC-01: inserted record can be read back
    @Test
    fun insert_thenReadAll_returnsRecord() = runBlocking {
        dao.insert(HealthRecord(date = "2026-11-12", steps = 7850, calories = 420, water = 2000))
        val all = dao.getAll().first()
        assertEquals(1, all.size)
        assertEquals(7850, all[0].steps)
    }

    // TC-02: update changes the stored values
    @Test
    fun update_changesValues() = runBlocking {
        dao.insert(HealthRecord(date = "2026-11-12", steps = 100, calories = 10, water = 500))
        val saved = dao.getAll().first()[0]
        dao.update(saved.copy(steps = 9999))
        assertEquals(9999, dao.getById(saved.id)?.steps)
    }

    // TC-03: delete removes the record
    @Test
    fun delete_removesRecord() = runBlocking {
        dao.insert(HealthRecord(date = "2026-11-12", steps = 100, calories = 10, water = 500))
        val saved = dao.getAll().first()[0]
        dao.delete(saved)
        assertNull(dao.getById(saved.id))
    }

    // TC-04: search by date text only returns matching records
    @Test
    fun searchByDate_filtersCorrectly() = runBlocking {
        dao.insert(HealthRecord(date = "2026-11-12", steps = 1, calories = 1, water = 1))
        dao.insert(HealthRecord(date = "2026-10-05", steps = 2, calories = 2, water = 2))
        val result = dao.searchByDate("2026-11").first()
        assertEquals(1, result.size)
        assertEquals("2026-11-12", result[0].date)
    }
}

