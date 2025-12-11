package com.example.uasmobileprogram.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.uasmobileprogram.data.model.Event
import com.example.uasmobileprogram.viewmodel.EventViewModel
import com.example.uasmobileprogram.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(
    viewModel: EventViewModel,
    onOpenDetail: (Int) -> Unit,
    onAdd: () -> Unit
) {
    val state by viewModel.events.collectAsState()

    // Load events once
    LaunchedEffect(Unit) {
        viewModel.fetchEvents()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Events") },
                actions = {
                    IconButton(onClick = onAdd) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add Event"
                        )
                    }
                }
            )
        }
    ) { inner ->
        Box(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
        ) {
            when (state) {
                is UiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is UiState.Error -> {
                    Text(
                        text = "Error: ${(state as UiState.Error).message}",
                        modifier = Modifier.padding(16.dp)
                    )
                }

                is UiState.Success -> {
                    val list = (state as UiState.Success<List<Event>>).data

                    if (list.isEmpty()) {
                        Box(
                            Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Tidak ada event.")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(8.dp)
                        ) {
                            items(list) { event ->
                                EventRow(
                                    event = event,
                                    onClick = { event.id?.let { onOpenDetail(it) } }
                                )
                            }
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
        modifier = Modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
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
