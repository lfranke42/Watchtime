package de.htwk.watchtime.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="episodes")
data class EpisodeDbEntry(
    @PrimaryKey
    val episodeId: Int,
    val seriesId: Int,
    val seasonNumber: Int,
    val runtime: Int?,
)
