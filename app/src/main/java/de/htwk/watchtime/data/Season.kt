package de.htwk.watchtime.data

data class Season(
    val id: Int,
    val seasonNumber: Int,
    val episodeIds: MutableList<Int>
)
