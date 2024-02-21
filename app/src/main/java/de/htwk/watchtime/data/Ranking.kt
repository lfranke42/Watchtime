package de.htwk.watchtime.data

data class Ranking(
    val position: Int,
    val totalWatchtime: Long,
    val closestNeighbors: List<RankingNeighbor>
)
