package com.example.healthmate.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtils {

    private const val PATTERN = "yyyy-MM-dd"

    fun format(calendar: Calendar): String {
        return SimpleDateFormat(PATTERN, Locale.US).format(calendar.time)
    }

    fun today(): String {
        return format(Calendar.getInstance())
    }

    fun daysAgo(days: Int): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -days)
        return format(calendar)
    }
}