package de.htwk.watchtime.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpisodeDto(
    @Json(name = "name") val name: String,
    @Json(name = "runtime") val runtime: Int?,
    @Json(name = "seasonNumber") val seasonNumber: Int,
    @Json(name = "number") val episodeNumber: Int,
    @Json(name = "id") val id: Int,
    @Json(name = "seriesId") val seriesId: Int,
    )
