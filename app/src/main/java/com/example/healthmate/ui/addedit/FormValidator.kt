package com.example.healthmate.ui.addedit

import com.example.healthmate.util.DateUtils

// Validation rules for the Add/Edit form. Each function returns an error
// message, or null when the input is valid. No Android code, so it is easy to test.
object FormValidator {

    // Upper limits that keep the numbers realistic
    const val MAX_STEPS = 100_000
    const val MAX_CALORIES = 10_000
    const val MAX_WATER = 10_000   // millilitres

    // Checks the date field: required, real date, not in the future
    fun validateDate(text: String): String? {
        if (text.isBlank()) return "Date is required"
        if (!DateUtils.isValidDate(text)) return "Use the format yyyy-MM-dd (e.g. 2026-11-13)"
        if (DateUtils.isFuture(text)) return "Date cannot be in the future"
        return null
    }

    // Checks a number field: required, whole number, not negative, not too large
    fun validateNumber(text: String, label: String, max: Int): String? {
        if (text.isBlank()) return "$label is required"
        val value = text.trim().toIntOrNull() ?: return "$label must be a whole number"
        if (value < 0) return "$label cannot be negative"
        if (value > max) return "$label is too high (maximum $max)"
        return null
    }
}

