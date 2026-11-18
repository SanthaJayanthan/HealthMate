package com.example.healthmate

import com.example.healthmate.data.local.HealthRecord
import com.example.healthmate.ui.dashboard.toDashboardState
import org.junit.Assert.assertEquals
import org.junit.Test

class DashboardStateTest {

    // TC-10: only today's records are added up; other days are ignored
    @Test
    fun toDashboardState_sumsOnlyTodaysRecords() {
        val records = listOf(
            HealthRecord(1, "2026-11-12", 1000, 100, 500),   // today
            HealthRecord(2, "2026-11-12", 2000, 200, 750),   // today (second entry)
            HealthRecord(3, "2026-11-11", 9999, 999, 9999)   // yesterday: must be ignored
        )
        val state = records.toDashboardState("2026-11-12")
        assertEquals(3000, state.steps)
        assertEquals(300, state.calories)
        assertEquals(1250, state.water)
    }

    // TC-11: no records for today gives zero totals (not a crash)
    @Test
    fun toDashboardState_noRecordsToday_returnsZeros() {
        val records = listOf(HealthRecord(1, "2026-11-10", 500, 50, 300))
        val state = records.toDashboardState("2026-11-12")
        assertEquals(0, state.steps)
        assertEquals(0, state.calories)
        assertEquals(0, state.water)
    }
}

