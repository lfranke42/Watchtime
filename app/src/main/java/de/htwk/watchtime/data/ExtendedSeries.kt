package de.htwk.watchtime.data

data class ExtendedSeries(
    val name: String,
    val id: Int,
    val year: String,
    val imageUrl: String?,
    val episodes: List<Episode>,
    val seasons: Map<Int, Season>,
    val description: String?,
    val genres: List<Genre>,
)
