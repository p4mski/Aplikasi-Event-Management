@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.uasmobileprogram.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.uasmobileprogram.data.model.Event
import com.example.uasmobileprogram.viewmodel.EventViewModel
import com.example.uasmobileprogram.viewmodel.UiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
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
    var status by remember { mutableStateOf("upcoming") }

    // Load Event untuk edit
    LaunchedEffect(eventId) {
        if (eventId != null) viewModel.fetchEventById(eventId)
    }

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

    val textFieldColors = TextFieldDefaults.colors(
        focusedIndicatorColor = Color.Black,
        unfocusedIndicatorColor = Color.DarkGray,
        focusedLabelColor = Color.Black,
        unfocusedLabelColor = Color.Black,
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        cursorColor = Color.Black
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Buat Event") },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(18.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            val fieldModifier = Modifier.fillMaxWidth()

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = fieldModifier,
                colors = textFieldColors
            )

            OutlinedTextField(
                value = date,
                onValueChange = { date = it },
                label = { Text("Date (YYYY-MM-DD)") },
                modifier = fieldModifier,
                colors = textFieldColors
            )

            OutlinedTextField(
                value = time,
                onValueChange = { time = it },
                label = { Text("Time (HH:MM:SS)") },
                modifier = fieldModifier,
                colors = textFieldColors
            )

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = fieldModifier,
                colors = textFieldColors
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") },
                modifier = fieldModifier,
                colors = textFieldColors
            )

            OutlinedTextField(
                value = capacityText,
                onValueChange = { capacityText = it.filter { ch -> ch.isDigit() } },
                label = { Text("Capacity") },
                modifier = fieldModifier,
                colors = textFieldColors
            )

            // DROPDOWN STATUS
            var expanded by remember { mutableStateOf(false) }

            Box {
                OutlinedTextField(
                    value = status,
                    onValueChange = {},
                    label = { Text("Status") },
                    modifier = fieldModifier,
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = Color.Black)
                        }
                    },
                    colors = textFieldColors
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    listOf("upcoming", "ongoing", "completed", "cancelled").forEach { st ->
                        DropdownMenuItem(
                            text = { Text(st, color = Color.Black) },
                            onClick = {
                                status = st
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = {
                    if (title.isBlank() || date.isBlank() || time.isBlank() || location.isBlank()) return@Button

                    val event = Event(
                        id = eventId,
                        title = title,
                        date = date,
                        time = time,
                        location = location,
                        description = description.ifBlank { null },
                        capacity = capacityText.toIntOrNull(),
                        status = status
                    )

                    if (eventId == null) {
                        viewModel.createEvent(event) { ok, _ -> if (ok) onSaved() }
                    } else {
                        viewModel.updateEvent(eventId, event) { ok, _ -> if (ok) onSaved() }
                    }
                }) {
                    Text("Simpan")
                }

                OutlinedButton(onClick = onCancel) {
                    Text("Batal")
                }
            }
        }
    }
}
