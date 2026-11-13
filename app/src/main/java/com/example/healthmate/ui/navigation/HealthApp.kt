package com.example.healthmate.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.healthmate.data.repository.HealthRepository
import com.example.healthmate.ui.HealthViewModelFactory
import com.example.healthmate.ui.addedit.AddEditScreen
import com.example.healthmate.ui.addedit.AddEditViewModel
import com.example.healthmate.ui.addedit.AddEditViewModelFactory
import com.example.healthmate.ui.dashboard.DashboardScreen
import com.example.healthmate.ui.dashboard.DashboardViewModel
import com.example.healthmate.ui.records.RecordListScreen
import com.example.healthmate.ui.records.RecordListViewModel
import kotlinx.coroutines.launch

// The screen addresses used by the navigation system
object Routes {
    const val DASHBOARD = "dashboard"
    const val RECORDS = "records"
    const val ADD_EDIT = "addedit/{recordId}"   // {recordId} is filled in when navigating

    // Builds the address for the form: -1 means "new record"
    fun addEdit(recordId: Int?) = "addedit/${recordId ?: -1}"
}

// One entry of the bottom navigation bar
private data class BottomItem(val route: String, val label: String, val icon: ImageVector)

// The whole app UI: bottom bar, add button, pop-up messages and the screens
@Composable
fun HealthApp(repository: HealthRepository) {
    val navController = rememberNavController()
    // Which screen is showing right now
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route
    // The bottom bar and add button appear only on the two main screens
    val onMainScreen = currentRoute == Routes.DASHBOARD || currentRoute == Routes.RECORDS

    // One factory shared by the ViewModels that only need the repository
    val factory = remember(repository) { HealthViewModelFactory(repository) }

    // Holds the pop-up message currently being shown (the "snackbar")
    val snackbarHostState = remember { SnackbarHostState() }
    // Needed to start the (suspending) showSnackbar call from a button click
    val scope = rememberCoroutineScope()
    // Shows a short pop-up message at the bottom of the screen
    val showMessage: (String) -> Unit = { text ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss() // replace any message still on screen
            snackbarHostState.showSnackbar(text)             // shows for a few seconds, then hides
        }
    }

    val items = listOf(
        BottomItem(Routes.DASHBOARD, "Dashboard", Icons.Filled.Home),
        BottomItem(Routes.RECORDS, "Records", Icons.Filled.DateRange)
    )

    Scaffold(
        // Where the pop-up messages appear
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (onMainScreen) {
                NavigationBar {
                    items.forEach { item ->
                        NavigationBarItem(
                            selected = currentRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(Routes.DASHBOARD) { saveState = true } // do not pile up screens
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (onMainScreen) {
                // Opens the form in "add new" mode
                FloatingActionButton(onClick = { navController.navigate(Routes.addEdit(null)) }) {
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
            // Screen 1: Dashboard
            composable(Routes.DASHBOARD) {
                val vm: DashboardViewModel = viewModel(factory = factory)
                DashboardScreen(vm)
            }
            // Screen 2: Records list; Edit opens the form, delete shows a pop-up message
            composable(Routes.RECORDS) {
                val vm: RecordListViewModel = viewModel(factory = factory)
                RecordListScreen(
                    viewModel = vm,
                    onEdit = { id -> navController.navigate(Routes.addEdit(id)) },
                    onMessage = showMessage
                )
            }
            // Screen 3: Add/Edit form
            composable(
                route = Routes.ADD_EDIT,
                arguments = listOf(navArgument("recordId") { type = NavType.IntType })
            ) { entry ->
                val id = entry.arguments?.getInt("recordId") ?: -1
                // -1 becomes null, which the ViewModel reads as "add new"
                val vm: AddEditViewModel = viewModel(
                    factory = AddEditViewModelFactory(repository, if (id == -1) null else id)
                )
                AddEditScreen(
                    viewModel = vm,
                    onBack = { navController.popBackStack() },       // back arrow: no message
                    onSaved = { wasEditing ->
                        navController.popBackStack()                  // leave the form
                        showMessage(if (wasEditing) "Record updated" else "Record added")
                    }
                )
            }
        }
    }
}