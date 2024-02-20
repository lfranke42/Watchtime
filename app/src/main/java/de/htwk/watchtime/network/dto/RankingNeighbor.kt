package de.htwk.watchtime.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RankingNeighbor (
    @Json(name = "position") val position: Int,
    @Json(name = "totalWatchtime") val totalWatchtime: Long
)

fun RankingNeighbor.toRankingNeighbor(): de.htwk.watchtime.data.RankingNeighbor {
    return de.htwk.watchtime.data.RankingNeighbor(
        position = position,
        totalWatchtime = totalWatchtime
    )
}