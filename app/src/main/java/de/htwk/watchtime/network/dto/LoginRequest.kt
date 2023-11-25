package de.htwk.watchtime.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequest (
    @Json(name = "apikey") val apiKey: String,
    @Json(name = "pin") val pin: String = ""
)