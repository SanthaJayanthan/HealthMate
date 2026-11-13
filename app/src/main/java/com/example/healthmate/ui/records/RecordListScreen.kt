package com.example.healthmate.ui.records

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.healthmate.data.local.HealthRecord
import com.example.healthmate.ui.theme.CaloriesColor
import com.example.healthmate.ui.theme.StepsColor
import com.example.healthmate.ui.theme.WaterColor

// Records list: search box, list of records, edit and delete buttons
@Composable
fun RecordListScreen(
    viewModel: RecordListViewModel,        // holds the list and the search text
    onEdit: (Int) -> Unit,                 // called with the record id when Edit is tapped
    onMessage: (String) -> Unit = {},      // called to show a pop-up message (e.g. after delete)
    modifier: Modifier = Modifier
) {
    // The list and the search text, both observed from the ViewModel
    val records by viewModel.records.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    // The record waiting for delete confirmation (null = no dialog shown)
    var recordToDelete by remember { mutableStateOf<HealthRecord?>(null) }

    Column(modifier = modifier.fillMaxSize().padding(horizontal = 16.dp)) {
        Text(
            "Records",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        // Search box: filters by date text such as 2026-11
        OutlinedTextField(
            value = query,
            onValueChange = viewModel::onSearchChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search by date (e.g. 2026-11)") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
            singleLine = true
        )

        if (records.isEmpty()) {
            // Empty state: nothing saved yet, or the search found nothing
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No records found")
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                // Extra bottom space so the add button never hides the last card
                contentPadding = PaddingValues(top = 12.dp, bottom = 88.dp)
            ) {
                items(records, key = { it.id }) { record ->
                    RecordCard(
                        record = record,
                        onEdit = { onEdit(record.id) },
                        onDelete = { recordToDelete = record }   // ask first, do not delete yet
                    )
                }
            }
        }
    }

    // Confirmation dialog shown only while recordToDelete has a value
    recordToDelete?.let { record ->
        AlertDialog(
            onDismissRequest = { recordToDelete = null },
            title = { Text("Delete record?") },
            text = { Text("Delete the record for ${record.date}? This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteRecord(record)   // really delete
                    onMessage("Record deleted")      // show the pop-up message
                    recordToDelete = null            // close the dialog
                }) { Text("Delete") }
            },
            dismissButton = { TextButton(onClick = { recordToDelete = null }) { Text("Cancel") } }
        )
    }
}

// One row of the list: date, three values, edit and delete buttons
@Composable
private fun RecordCard(record: HealthRecord, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left side takes all spare width; the buttons stay on the right
            Column(modifier = Modifier.weight(1f)) {
                Text(record.date, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricLabel(Icons.AutoMirrored.Filled.DirectionsWalk, "${record.steps}", StepsColor)
                    MetricLabel(Icons.Filled.LocalFireDepartment, "${record.calories} kcal", CaloriesColor)
                    MetricLabel(Icons.Filled.WaterDrop, "${record.water} ml", WaterColor)
                }
            }
            IconButton(onClick = onEdit) { Icon(Icons.Filled.Edit, contentDescription = "Edit record") }
            IconButton(onClick = onDelete) { Icon(Icons.Filled.Delete, contentDescription = "Delete record") }
        }
    }
}

// Small coloured icon followed by a value, e.g. a flame and "420 kcal"
@Composable
private fun MetricLabel(icon: ImageVector, text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(4.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}