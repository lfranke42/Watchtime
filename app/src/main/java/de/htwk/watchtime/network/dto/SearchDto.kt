package de.htwk.watchtime.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchDto(
    @Json(name = "name") val name: String,
    @Json(name = "tvdb_id") val id: Int,
    @Json(name = "year") val year: String?,
    @Json(name = "image_url") val imageUrl: String?,
    @Json(name = "type") val type: String
)
