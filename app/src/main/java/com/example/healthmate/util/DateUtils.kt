package com.example.healthmate.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Helper for the date text format used by the whole app: yyyy-MM-dd
object DateUtils {

    // Pattern the database stores and the search box expects
    private const val PATTERN = "yyyy-MM-dd"

    // Today's date as text, e.g. "2026-11-12"
    fun today(): String = daysAgo(0)

    // The date N days before today as text (daysAgo(1) = yesterday)
    fun daysAgo(days: Int): String {
        // New formatter each call because SimpleDateFormat is not thread-safe
        val formatter = SimpleDateFormat(PATTERN, Locale.US)
        val calendar = Calendar.getInstance()          // starts at "now"
        calendar.add(Calendar.DAY_OF_YEAR, -days)      // move back N days
        return formatter.format(calendar.time)
    }
}

