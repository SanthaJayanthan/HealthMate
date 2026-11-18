package com.example.healthmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.healthmate.data.local.AppDatabase
import com.example.healthmate.data.local.GoalPreferences
import com.example.healthmate.data.repository.HealthRepository
import com.example.healthmate.ui.navigation.HealthApp
import com.example.healthmate.ui.theme.HealthMateTheme
import kotlinx.coroutines.launch

// The only Activity: it builds the database, repository and goals, then shows the app
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()                                   // draw behind the status bar

        // Database -> DAO -> repository (the layers from our architecture)
        val database = AppDatabase.getInstance(applicationContext)
        val repository = HealthRepository(database.healthRecordDao())

        // The user's saved daily targets (stored on the phone)
        val goalPreferences = GoalPreferences(applicationContext)

        // First run only: fill the empty database with the 10 sample records
        lifecycleScope.launch { repository.seedIfEmpty() }

        // Show the screens inside the app theme
        setContent {
            HealthMateTheme {
                HealthApp(repository, goalPreferences)
            }
        }
    }
}