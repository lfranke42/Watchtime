package de.htwk.watchtime.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SeriesExtendedResponse(
    @Json(name = "data") val data: SeriesExtendedDto
)
