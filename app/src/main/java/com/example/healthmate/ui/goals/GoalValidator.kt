package com.example.healthmate.ui.goals

// Validation rules for the Goals form. Returns an error message, or null when valid.
object GoalValidator {

    // Upper limits that keep the targets realistic
    const val MAX_STEPS = 100_000
    const val MAX_CALORIES = 10_000
    const val MAX_WATER = 10_000   // millilitres

    // A goal of zero makes no sense (it would also divide by zero in the progress bar)
    const val MIN_GOAL = 1

    // Checks one goal field: required, whole number, at least 1, not above the maximum
    fun validate(text: String, label: String, max: Int): String? {
        if (text.isBlank()) return "$label goal is required"
        val value = text.trim().toIntOrNull() ?: return "$label goal must be a whole number"
        if (value < MIN_GOAL) return "$label goal must be at least $MIN_GOAL"
        if (value > max) return "$label goal is too high (maximum $max)"
        return null
    }
}

