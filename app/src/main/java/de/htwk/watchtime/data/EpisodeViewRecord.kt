package de.htwk.watchtime.data

data class EpisodeViewRecord(
    val episodeId: Int,
    val seasonNumber: Int,
    val genres: List<Genre>,
    val runtime: Int,
)
