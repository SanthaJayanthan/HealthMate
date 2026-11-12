package com.example.healthmate.data.local

import com.example.healthmate.util.DateUtils

// Sample records used to fill the database the first time the app runs
object DummyData {

    // Fixed lists so the sample data is the same on every install
    private val steps = listOf(7850, 6200, 9100, 4300, 10250, 7600, 5400, 8800, 6900, 7300)
    private val calories = listOf(420, 350, 510, 240, 560, 400, 300, 480, 380, 410)
    private val water = listOf(2000, 1500, 2500, 1200, 2800, 1900, 1600, 2200, 1800, 2000)

    // Builds 10 records: index 0 = today, 1 = yesterday, ... 9 = nine days ago
    fun generate(): List<HealthRecord> = steps.indices.map { i ->
        HealthRecord(
            date = DateUtils.daysAgo(i),
            steps = steps[i],
            calories = calories[i],
            water = water[i]
        )
    }
}

