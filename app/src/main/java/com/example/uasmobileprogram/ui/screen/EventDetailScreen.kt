package com.example.uasmobileprogram.ui.screen

import android.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.uasmobileprogram.viewmodel.EventViewModel
import com.example.uasmobileprogram.viewmodel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(
    eventId: Int,
    viewModel: EventViewModel,
    onEdit: () -> Unit,
    onBack: () -> Unit
) {
    val state by viewModel.selectedEvent.collectAsState()
    var showDeleteConfirm by remember { mutableStateOf(false) }

    // Dropdown
    var expanded by remember { mutableStateOf(false) }
    var newStatus by remember { mutableStateOf("") }

    // Load event
    LaunchedEffect(eventId) {
        viewModel.fetchEventById(eventId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Event Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = onEdit) {
                        Text("Edit", color = Color.White)   // 🔥 Warna Edit
                    }
                    TextButton(onClick = { showDeleteConfirm = true }) {
                        Text("Delete", color = Color.White)     // 🔥 Warna Delete
                    }
                }
            )
        }
    ) { padding ->

        Box(
            Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            when (state) {

                is UiState.Loading -> CircularProgressIndicator()

                is UiState.Error ->
                    Text("Error: ${(state as UiState.Error).message}")

                is UiState.Success -> {
                    val e = (state as UiState.Success).data
                    newStatus = e.status

                    Column {
                        Spacer(modifier = Modifier.height(16.dp))

                        // 🔳 BOX DETAIL EVENT
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Text(e.title, style = MaterialTheme.typography.titleLarge)
                                Spacer(Modifier.height(8.dp))
                                Text("Tanggal: ${e.date} ${e.time}")
                                Text("Lokasi: ${e.location}")
                                Spacer(Modifier.height(8.dp))
                                Text("Deskripsi:")
                                Text(e.description ?: "-")
                                Spacer(Modifier.height(8.dp))
                                Text("Kapasitas: ${e.capacity ?: "-"}")
                                Spacer(Modifier.height(8.dp))
                                Text("Status: ${e.status ?: "-"}")

                            }
                        }
                    }
                }
            }
        }
    }

    // 🔥 DIALOG KONFIRMASI DELETE
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = {
                Text(
                    "Hapus Event?",
                    color = Color.Black,
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = { Text("Event ini akan dihapus secara permanen.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteEvent(eventId) { _, _ -> }
                        showDeleteConfirm = false
                        onBack()
                    }
                ) { Text("Hapus", color = Color.Red) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Batal")
                }
            }
        )
    }
}
