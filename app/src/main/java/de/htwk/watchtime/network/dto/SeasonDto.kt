package de.htwk.watchtime.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SeasonDto (
    @Json(name = "number") val seasonNumber: Int,
    @Json(name = "id") val id: Int,
)
