package com.example.healthmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.healthmate.data.local.AppDatabase
import com.example.healthmate.data.repository.HealthRepository
import com.example.healthmate.ui.HealthMateApp
import com.example.healthmate.ui.theme.HealthMateTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = HealthRepository(database.healthRecordDao())
        lifecycleScope.launch { repository.seedIfEmpty() }
        enableEdgeToEdge()
        setContent {
            HealthMateTheme {
                HealthMateApp(repository)
            }
        }
    }
}