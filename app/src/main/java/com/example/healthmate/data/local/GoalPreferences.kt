package com.example.healthmate.data.local

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Default targets, used until the user saves their own
const val DEFAULT_STEPS_GOAL = 10_000
const val DEFAULT_CALORIES_GOAL = 500
const val DEFAULT_WATER_GOAL = 2_500   // millilitres

// The three daily targets
data class Goals(
    val steps: Int = DEFAULT_STEPS_GOAL,
    val calories: Int = DEFAULT_CALORIES_GOAL,
    val water: Int = DEFAULT_WATER_GOAL
)

// Saves and loads the goals in a small file on the phone (SharedPreferences).
// The goals are also exposed as a flow, so screens update when they change.
class GoalPreferences(private val prefs: SharedPreferences) {

    // Convenience constructor used by the app: opens the real preferences file
    constructor(context: Context) : this(
        context.applicationContext.getSharedPreferences("healthmate_goals", Context.MODE_PRIVATE)
    )

    // Current goals; starts with whatever was saved last time (or the defaults)
    private val _goals = MutableStateFlow(read())
    val goals: StateFlow<Goals> = _goals

    // Writes the goals to the file and tells every screen watching the flow
    fun save(goals: Goals) {
        prefs.edit()
            .putInt(KEY_STEPS, goals.steps)
            .putInt(KEY_CALORIES, goals.calories)
            .putInt(KEY_WATER, goals.water)
            .apply()
        _goals.value = goals
    }

    // Reads the saved values, falling back to the defaults for anything missing
    private fun read() = Goals(
        steps = prefs.getInt(KEY_STEPS, DEFAULT_STEPS_GOAL),
        calories = prefs.getInt(KEY_CALORIES, DEFAULT_CALORIES_GOAL),
        water = prefs.getInt(KEY_WATER, DEFAULT_WATER_GOAL)
    )

    private companion object {
        // Names of the stored values
        const val KEY_STEPS = "goal_steps"
        const val KEY_CALORIES = "goal_calories"
        const val KEY_WATER = "goal_water"
    }
}

