package de.htwk.watchtime.network.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import de.htwk.watchtime.data.Ranking

@JsonClass(generateAdapter = true)
data class RankingResponse(
    @Json(name = "position") val position: Int,
    @Json(name = "totalWatchtime") val totalWatchtime: Long,
    @Json(name = "closestNeighbors") val closestNeighbors: List<RankingNeighbor>
)

fun RankingResponse.toRanking(): Ranking {
    return Ranking(
        position = position,
        totalWatchtime = totalWatchtime,
        closestNeighbors = closestNeighbors.map { it.toRankingNeighbor() }
    )
}