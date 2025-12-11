package com.example.uasmobileprogram.data.model

import com.google.gson.annotations.SerializedName

data class Event(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("title") val title: String,
    @SerializedName("date") val date: String,        // format YYYY-MM-DD
    @SerializedName("time") val time: String,        // format HH:MM:SS
    @SerializedName("location") val location: String,
    @SerializedName("description") val description: String? = null,
    @SerializedName("capacity") val capacity: Int? = null,
    @SerializedName("status") val status: String,    // upcoming|ongoing|completed|cancelled
)
