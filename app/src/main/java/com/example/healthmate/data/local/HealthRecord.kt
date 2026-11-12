package com.example.healthmate.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// @Entity makes this class a database table; the table name matches the brief
@Entity(tableName = "health_records")
data class HealthRecord(
    // Primary key; autoGenerate lets the database assign 1, 2, 3...
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    // Date stored as text in the format yyyy-MM-dd (e.g. "2026-11-12")
    val date: String,
    // Number of steps walked that day
    val steps: Int,
    // Calories burned that day (kcal)
    val calories: Int,
    // Water intake that day in millilitres
    val water: Int
)