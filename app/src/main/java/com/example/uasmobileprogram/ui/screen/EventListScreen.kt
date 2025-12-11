package com.example.uasmobileprogram.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.uasmobileprogram.data.model.Event
import com.example.uasmobileprogram.viewmodel.EventViewModel
import com.example.uasmobileprogram.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(
    viewModel: EventViewModel,
    onAdd: () -> Unit,
    onDetail: (Int) -> Unit
) {
    val state by viewModel.events.collectAsState()

    var searchName by remember { mutableStateOf("") }
    var searchDate by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.fetchEvents()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Events") },
                actions = {
                    IconButton(onClick = onAdd) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(12.dp)
        ) {

            // 🔍 Search by name
            OutlinedTextField(
                value = searchName,
                onValueChange = { searchName = it },
                label = { Text("Cari nama event") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(8.dp))

            // 🔍 Search by date
            OutlinedTextField(
                value = searchDate,
                onValueChange = { searchDate = it },
                label = { Text("Cari berdasarkan tanggal (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            when (state) {
                is UiState.Loading -> CircularProgressIndicator()
                is UiState.Error -> Text("Error: ${(state as UiState.Error).message}")
                is UiState.Success -> {
                    val events = (state as UiState.Success).data

                    // 🔥 FILTER LIST
                    val filtered = events.filter {
                        (it.title.contains(searchName, ignoreCase = true)) &&
                                (it.date.contains(searchDate, ignoreCase = true))
                    }

                    LazyColumn {
                        items(filtered) { event ->
                            EventCard(event) {
                                onDetail(event.id!!)
                            }
                            Spacer(Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EventRow(event: Event, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) { Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${event.date} • ${event.time}",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = event.location,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Status: ${event.status}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun EventCard(event: Event, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(event.title, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text("${event.date} - ${event.time}")
            Text(event.location)
            Text("Status: ${event.status}")
        }
    }
}
