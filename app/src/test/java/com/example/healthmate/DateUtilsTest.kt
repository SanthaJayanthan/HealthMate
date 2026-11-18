package com.example.healthmate

import com.example.healthmate.data.local.DummyData
import com.example.healthmate.util.DateUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class DateUtilsTest {

    // TC-05: dates use the yyyy-MM-dd format (10 chars, dashes in the right places)
    @Test
    fun today_hasCorrectFormat() {
        val today = DateUtils.today()
        assertEquals(10, today.length)
        assertEquals('-', today[4])
        assertEquals('-', today[7])
    }

    // TC-06: yesterday is a different date from today
    @Test
    fun daysAgo_returnsDifferentDate() {
        assertNotEquals(DateUtils.today(), DateUtils.daysAgo(1))
    }

    // TC-07: dummy data has 10 records and the first one is today
    @Test
    fun dummyData_hasTenRecords_startingToday() {
        val records = DummyData.generate()
        assertEquals(10, records.size)
        assertEquals(DateUtils.today(), records[0].date)
    }
}