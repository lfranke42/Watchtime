package de.htwk.watchtime.data

data class Episode(
    val name: String,
    val runtime: Int,
    val id: Int,
    val seasonNumber: Int,
    val episodeNumber: Int,
)
