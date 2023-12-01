package de.htwk.watchtime.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SeriesEpisodesDto (
    @Json(name = "episodes") val episodes: List<EpisodeDto>
)
