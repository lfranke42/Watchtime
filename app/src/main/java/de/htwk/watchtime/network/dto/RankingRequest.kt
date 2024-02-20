package de.htwk.watchtime.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RankingRequest (
    @Json(name = "userId") val userId: String,
    @Json(name ="totalWatchtime") val totalWatchtime: Long
)
