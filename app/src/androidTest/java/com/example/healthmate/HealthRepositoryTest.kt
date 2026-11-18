package com.example.healthmate

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.healthmate.data.local.AppDatabase
import com.example.healthmate.data.repository.HealthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HealthRepositoryTest {
    private lateinit var db: AppDatabase
    private lateinit var repository: HealthRepository

    // Fresh in-memory database before every test
    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        repository = HealthRepository(db.healthRecordDao())
    }

    @After
    fun tearDown() = db.close()

    // TC-08: seeding an empty database adds the 10 dummy records
    @Test
    fun seedIfEmpty_insertsTenRecords() = runBlocking {
        repository.seedIfEmpty()
        assertEquals(10, repository.allRecords.first().size)
    }

    // TC-09: seeding twice does not create duplicates
    @Test
    fun seedIfEmpty_calledTwice_noDuplicates() = runBlocking {
        repository.seedIfEmpty()
        repository.seedIfEmpty()
        assertEquals(10, repository.allRecords.first().size)
    }
}

