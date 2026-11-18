package com.example.healthmate

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.healthmate.data.local.GoalPreferences
import com.example.healthmate.data.local.Goals
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GoalPreferencesTest {
    private lateinit var sharedPrefs: SharedPreferences

    // Uses a separate test file (cleared first) so the real app goals are never touched
    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        sharedPrefs = context.getSharedPreferences("goals_test", Context.MODE_PRIVATE)
        sharedPrefs.edit().clear().commit()
    }

    // TC-45: with nothing saved, the default goals are used
    @Test
    fun noSavedGoals_returnsDefaults() {
        assertEquals(Goals(), GoalPreferences(sharedPrefs).goals.value)
    }

    // TC-46: saved goals are exposed straight away
    @Test
    fun save_updatesCurrentGoals() {
        val prefs = GoalPreferences(sharedPrefs)
        prefs.save(Goals(steps = 8000, calories = 400, water = 2000))
        assertEquals(Goals(8000, 400, 2000), prefs.goals.value)
    }

    // TC-47: saved goals survive "closing the app" (a brand new object reads the same file)
    @Test
    fun savedGoals_arePersisted() {
        GoalPreferences(sharedPrefs).save(Goals(steps = 7000, calories = 350, water = 1800))
        val reloaded = GoalPreferences(sharedPrefs)
        assertEquals(Goals(7000, 350, 1800), reloaded.goals.value)
    }
}

