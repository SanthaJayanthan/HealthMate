package com.example.healthmate.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Lists the tables (entities) and the schema version
@Database(entities = [HealthRecord::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Room generates the implementation of this DAO for us
    abstract fun healthRecordDao(): HealthRecordDao

    companion object {
        // @Volatile makes changes visible to all threads immediately
        @Volatile
        private var INSTANCE: AppDatabase? = null

        // Singleton: only one database object exists in the whole app
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "healthmate.db" // file name of the SQLite database
                ).build().also { INSTANCE = it }
            }
        }
    }
}

