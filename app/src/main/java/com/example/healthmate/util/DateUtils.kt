package com.example.healthmate.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

// Helper for the date text format used by the whole app: yyyy-MM-dd
object DateUtils {

    // Pattern the database stores and the search box expects
    private const val PATTERN = "yyyy-MM-dd"

    // Exact shape of a valid date text: 4 digits, dash, 2 digits, dash, 2 digits
    private val DATE_SHAPE = Regex("\\d{4}-\\d{2}-\\d{2}")

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

    // True only if the text is a real calendar date in yyyy-MM-dd format
    fun isValidDate(text: String): Boolean {
        // Reject anything that does not look like 2026-11-13
        if (!DATE_SHAPE.matches(text)) return false
        val formatter = SimpleDateFormat(PATTERN, Locale.US)
        formatter.isLenient = false                    // reject impossible dates like 2026-02-31
        return try {
            formatter.parse(text) != null
        } catch (e: ParseException) {
            false
        }
    }

    // True if the date is after today. Plain text comparison works
    // because yyyy-MM-dd sorts in the same order as the calendar.
    fun isFuture(text: String): Boolean = text > today()
}