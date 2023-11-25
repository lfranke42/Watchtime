package de.htwk.watchtime.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SeriesDto(
    @Json(name = "name") val name: String,
    @Json(name = "lastUpdated") val year: String,
    @Json(name = "image") val imageUrl: String?,
)
