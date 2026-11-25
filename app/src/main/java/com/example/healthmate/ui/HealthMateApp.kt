package com.example.healthmate.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.healthmate.data.repository.HealthRepository
import com.example.healthmate.ui.addedit.AddEditScreen
import com.example.healthmate.ui.addedit.AddEditViewModel
import com.example.healthmate.ui.dashboard.DashboardScreen
import com.example.healthmate.ui.dashboard.DashboardViewModel
import com.example.healthmate.ui.records.RecordListScreen
import com.example.healthmate.ui.records.RecordListViewModel
import kotlinx.coroutines.launch

object Routes {
    const val DASHBOARD = "dashboard"
    const val RECORDS = "records"
    const val ADD = "add"
    const val EDIT = "edit/{id}"

    fun edit(id: Int): String = "edit/$id"
}

@Composable
fun HealthMateApp(repository: HealthRepository) {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    val showBars = currentRoute == Routes.DASHBOARD || currentRoute == Routes.RECORDS
    val factory = remember { HealthViewModelFactory(repository) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (showBars) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == Routes.DASHBOARD,
                        onClick = { navigateToTab(navController, Routes.DASHBOARD) },
                        icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                        label = { Text("Dashboard") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == Routes.RECORDS,
                        onClick = { navigateToTab(navController, Routes.RECORDS) },
                        icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) },
                        label = { Text("Records") }
                    )
                }
            }
        },
        floatingActionButton = {
            if (showBars) {
                FloatingActionButton(onClick = { navController.navigate(Routes.ADD) }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add record")
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.DASHBOARD,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.DASHBOARD) {
                val viewModel: DashboardViewModel = viewModel(factory = factory)
                DashboardScreen(viewModel = viewModel)
            }
            composable(Routes.RECORDS) {
                val viewModel: RecordListViewModel = viewModel(factory = factory)
                RecordListScreen(
                    viewModel = viewModel,
                    onEdit = { id -> navController.navigate(Routes.edit(id)) },
                    onDeleted = {
                        scope.launch { snackbarHostState.showSnackbar("Record deleted") }
                    }
                )
            }
            composable(Routes.ADD) {
                val viewModel: AddEditViewModel =
                    viewModel(factory = HealthViewModelFactory(repository, 0))
                AddEditScreen(
                    viewModel = viewModel,
                    onSaved = {
                        navController.popBackStack()
                        scope.launch { snackbarHostState.showSnackbar("Record added") }
                    }
                )
            }
            composable(
                route = Routes.EDIT,
                arguments = listOf(navArgument("id") { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt("id") ?: 0
                val viewModel: AddEditViewModel =
                    viewModel(factory = HealthViewModelFactory(repository, id))
                AddEditScreen(
                    viewModel = viewModel,
                    onSaved = {
                        navController.popBackStack()
                        scope.launch { snackbarHostState.showSnackbar("Record updated") }
                    }
                )
            }
        }
    }
}

private fun navigateToTab(navController: NavHostController, route: String) {
    navController.navigate(route) {
        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}