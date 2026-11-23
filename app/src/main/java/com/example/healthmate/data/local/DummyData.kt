package com.example.healthmate.data.local

import com.example.healthmate.util.DateUtils

object DummyData {

    fun generate(): List<HealthRecord> {
        val steps = listOf(7850, 9200, 6400, 10500, 5300, 8700, 11200, 7600, 4900, 9800)
        val calories = listOf(420, 510, 350, 560, 290, 470, 600, 410, 270, 530)
        val water = listOf(2000, 2400, 1800, 2600, 1500, 2200, 2800, 2000, 1600, 2500)

        return List(10) { index ->
            HealthRecord(
                date = DateUtils.daysAgo(index),
                steps = steps[index],
                calories = calories[index],
                water = water[index]
            )
        }
    }
}