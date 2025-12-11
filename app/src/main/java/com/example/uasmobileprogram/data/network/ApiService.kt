package com.example.uasmobileprogram.data.network

import com.example.uasmobileprogram.data.model.Event
import retrofit2.Response
import retrofit2.http.*

data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T?,
    val timestamp: String?
)

interface ApiService {

    // Get all events (returns ApiResponse<List<Event>>)
    @GET("api.php")
    suspend fun getAllEvents(
        @Query("date") date: String? = null,
        @Query("date_from") dateFrom: String? = null,
        @Query("date_to") dateTo: String? = null,
        @Query("status") status: String? = null,
        @Query("id") id: Int? = null,
        @Query("stats") stats: Int? = null
    ): Response<ApiResponse<List<Event>>>

    // Get single event by id (server supports id query param)
    @GET("api.php")
    suspend fun getEventById(@Query("id") id: Int): Response<ApiResponse<Event>>

    // Create event (POST JSON)
    @POST("api.php")
    suspend fun createEvent(@Body event: Event): Response<ApiResponse<Event>>

    // Update event (PUT, id as query)
    @PUT("api.php")
    suspend fun updateEvent(@Query("id") id: Int, @Body event: Event): Response<ApiResponse<Event>>

    // Delete event
    @DELETE("api.php")
    suspend fun deleteEvent(@Query("id") id: Int): Response<ApiResponse<Any>>
}
