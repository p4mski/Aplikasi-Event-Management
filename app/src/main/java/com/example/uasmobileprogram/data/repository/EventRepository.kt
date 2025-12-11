package com.example.uasmobileprogram.data.repository

import com.example.uasmobileprogram.data.model.Event
import com.example.uasmobileprogram.data.network.RetrofitInstance
import retrofit2.Response

class EventRepository {
    private val api = RetrofitInstance.api

    suspend fun getAllEvents() = api.getAllEvents()

    suspend fun getEventById(id: Int) = api.getEventById(id)

    suspend fun createEvent(event: Event) = api.createEvent(event)

    suspend fun updateEvent(id: Int, event: Event) = api.updateEvent(id, event)

    suspend fun deleteEvent(id: Int) = api.deleteEvent(id)
}
