package com.example.healthmate.ui.addedit

import com.example.healthmate.util.DateUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

object FormValidator {

    private const val MAX_STEPS = 100000L
    private const val MAX_CALORIES = 10000L
    private const val MAX_WATER = 10000L

    fun validateDate(input: String): String? {
        val value = input.trim()
        if (value.isEmpty()) return "Date is required"
        if (!Regex("\\d{4}-\\d{2}-\\d{2}").matches(value)) return "Use the format yyyy-MM-dd"

        val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        format.isLenient = false
        try {
            format.parse(value)
        } catch (e: ParseException) {
            return "Enter a real date"
        }

        if (value > DateUtils.today()) return "Date cannot be in the future"
        return null
    }

    fun validateSteps(input: String): String? {
        return validateNumber(input, "Steps", MAX_STEPS)
    }

    fun validateCalories(input: String): String? {
        return validateNumber(input, "Calories", MAX_CALORIES)
    }

    fun validateWater(input: String): String? {
        return validateNumber(input, "Water", MAX_WATER)
    }

    private fun validateNumber(input: String, name: String, max: Long): String? {
        val value = input.trim()
        if (value.isEmpty()) return "$name is required"
        val number = value.toLongOrNull() ?: return "$name must be a whole number"
        if (number < 0) return "$name cannot be negative"
        if (number > max) return "$name cannot be more than $max"
        return null
    }
}