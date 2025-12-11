package com.example.uasmobileprogram.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.uasmobileprogram.viewmodel.EventViewModel
import com.example.uasmobileprogram.viewmodel.UiState
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: Int,
    viewModel: EventViewModel,
    onEdit: () -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.selectedEvent.collectAsState()

    LaunchedEffect(eventId) {
        viewModel.fetchEventById(eventId)
    }

    var showDeleteConfirm by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
        TopAppBar(
            title = { Text("Event Detail") },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White
            )
        )

    }, containerColor = MaterialTheme.colorScheme.background)
    { inner ->
        Box(modifier = Modifier.padding(inner).padding(12.dp)) {
            when (state) {
                is UiState.Loading -> {
                    CircularProgressIndicator()
                }
                is UiState.Error -> {
                    Text("Error: ${(state as UiState.Error).message}")
                }
                is UiState.Success -> {
                    val e = (state as UiState.Success).data
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = e.title, style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Tanggal: ${e.date} ${e.time}")
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Lokasi: ${e.location}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Deskripsi:")
                        Text(text = e.description ?: "-")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Kapasitas: ${e.capacity ?: "-"}")
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Status: ${e.status}")
                    }
                }
            }

            if (showDeleteConfirm) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirm = false },
                    confirmButton = {
                        TextButton(onClick = {
                            viewModel.deleteEvent(eventId) { ok, msg ->
                                showDeleteConfirm = false
                                if (ok) onBack()
                                // else show snackbar? keep simple
                            }
                        }) { Text("Hapus") }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDeleteConfirm = false }) { Text("Batal") }
                    },
                    title = { Text("Konfirmasi") },
                    text = { Text("Hapus event ini?") }
                )
            }
        }
    }
}
