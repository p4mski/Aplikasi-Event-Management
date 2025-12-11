package com.example.uasmobileprogram.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uasmobileprogram.data.model.Event
import com.example.uasmobileprogram.data.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class UiState<out T> {
    object Loading: UiState<Nothing>()
    data class Success<T>(val data: T): UiState<T>()
    data class Error(val message: String): UiState<Nothing>()
}

class EventViewModel(
    private val repository: EventRepository = EventRepository()
) : ViewModel() {

    private val _events = MutableStateFlow<UiState<List<Event>>>(UiState.Loading)
    val events: StateFlow<UiState<List<Event>>> = _events

    private val _selectedEvent = MutableStateFlow<UiState<Event>>(UiState.Loading)
    val selectedEvent: StateFlow<UiState<Event>> = _selectedEvent

    fun fetchEvents() {
        viewModelScope.launch {
            _events.value = UiState.Loading
            try {
                val resp = repository.getAllEvents()
                if (resp.isSuccessful) {
                    val body = resp.body()
                    _events.value = UiState.Success(body?.data ?: emptyList())
                } else {
                    _events.value = UiState.Error("Gagal mengambil events: ${resp.code()}")
                }
            } catch (e: Exception) {
                _events.value = UiState.Error(e.localizedMessage ?: "Unknown")
            }
        }
    }

    fun fetchEventById(id: Int) {
        viewModelScope.launch {
            _selectedEvent.value = UiState.Loading
            try {
                val resp = repository.getEventById(id)
                if (resp.isSuccessful) {
                    val body = resp.body()
                    if (body?.data != null) {
                        _selectedEvent.value = UiState.Success(body.data)
                    } else {
                        _selectedEvent.value = UiState.Error(body?.message ?: "Event tidak ditemukan")
                    }
                } else {
                    _selectedEvent.value = UiState.Error("Gagal mengambil event: ${resp.code()}")
                }
            } catch (e: Exception) {
                _selectedEvent.value = UiState.Error(e.localizedMessage ?: "Unknown")
            }
        }
    }

    fun createEvent(event: Event, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val resp = repository.createEvent(event)
                if (resp.isSuccessful) {
                    onResult(true, null)
                    fetchEvents()
                } else {
                    onResult(false, "Error: ${resp.code()}")
                }
            } catch (e: Exception) {
                onResult(false, e.localizedMessage)
            }
        }
    }

    fun updateEvent(id: Int, event: Event, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val resp = repository.updateEvent(id, event)
                if (resp.isSuccessful) {
                    onResult(true, null)
                    fetchEvents()
                } else {
                    onResult(false, "Error: ${resp.code()}")
                }
            } catch (e: Exception) {
                onResult(false, e.localizedMessage)
            }
        }
    }

    fun deleteEvent(id: Int, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val resp = repository.deleteEvent(id)
                if (resp.isSuccessful) {
                    onResult(true, null)
                    fetchEvents()
                } else {
                    onResult(false, "Error: ${resp.code()}")
                }
            } catch (e: Exception) {
                onResult(false, e.localizedMessage)
            }
        }
    }
}
