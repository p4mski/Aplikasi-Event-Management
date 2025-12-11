@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.uasmobileprogram.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.uasmobileprogram.data.model.Event
import com.example.uasmobileprogram.viewmodel.EventViewModel
import com.example.uasmobileprogram.viewmodel.UiState
import kotlinx.coroutines.launch

@Composable
fun EventFormScreen(
    viewModel: EventViewModel,
    eventId: Int?,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val selected by viewModel.selectedEvent.collectAsState()

    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var capacityText by remember { mutableStateOf("") }

    // dropdown value
    var status by remember { mutableStateOf("upcoming") }

    // Load event when editing
    LaunchedEffect(eventId) {
        if (eventId != null) {
            viewModel.fetchEventById(eventId)
        }
    }

    // Fill fields when event loaded
    LaunchedEffect(selected) {
        if (selected is UiState.Success) {
            val e = (selected as UiState.Success).data
            title = e.title
            date = e.date
            time = e.time
            location = e.location
            description = e.description ?: ""
            capacityText = e.capacity?.toString() ?: ""
            status = e.status
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buat Event") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Date (YYYY-MM-DD)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = time,
                onValueChange = { time = it },
                label = { Text("Time (HH:MM:SS)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = capacityText,
                onValueChange = { capacityText = it.filter { ch -> ch.isDigit() } },
                label = { Text("Capacity") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            // ⬇⬇⬇ REPLACE STATUS INPUT WITH DROPDOWN ⬇⬇⬇
            StatusDropdown(
                value = status,
                onValueChange = { status = it }
            )
            // ⬆⬆⬆ END DROPDOWN ⬆⬆⬆

            Spacer(Modifier.height(16.dp))

            Row {
                Button(
                    onClick = {
                        if (title.isBlank() || date.isBlank() || time.isBlank() || location.isBlank()) {
                            return@Button
                        }

                        val event = Event(
                            id = eventId,
                            title = title,
                            date = date,
                            time = time,
                            location = location,
                            description = if (description.isBlank()) null else description,
                            capacity = capacityText.toIntOrNull(),
                            status = status
                        )

                        if (eventId == null) {
                            viewModel.createEvent(event) { ok, _ ->
                                if (ok) onSaved()
                            }
                        } else {
                            viewModel.updateEvent(eventId, event) { ok, _ ->
                                if (ok) onSaved()
                            }
                        }
                    }
                ) {
                    Text("Simpan")
                }

                Spacer(Modifier.width(8.dp))

                OutlinedButton(onClick = onCancel) {
                    Text("Batal")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatusDropdown(
    value: String,
    onValueChange: (String) -> Unit
) {
    val items = listOf("upcoming", "ongoing", "completed", "cancelled")

    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { },
            readOnly = true,
            label = { Text("Status") },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = Color.DarkGray,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item, color = Color.Black) },
                    onClick = {
                        onValueChange(item)
                        expanded = false
                    }
                )
            }
        }
    }
}
