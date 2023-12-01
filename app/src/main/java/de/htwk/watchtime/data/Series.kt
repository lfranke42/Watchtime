package de.htwk.watchtime.data

data class Series(
    val name: String,
    val id: Int,
    val year: String,
    val imageUrl: String?,
    val episodes: List<Episode>?,
)
