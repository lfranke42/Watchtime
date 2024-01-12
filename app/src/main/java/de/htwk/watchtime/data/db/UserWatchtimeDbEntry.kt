package de.htwk.watchtime.data.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "userWatchtime",
    foreignKeys = [ForeignKey(
        entity = SeriesDbEntry::class,
        parentColumns = arrayOf("seriesId"),
        childColumns = arrayOf("seriesId"),
        onDelete = ForeignKey.CASCADE
    ), ForeignKey(
        entity = EpisodeDbEntry::class,
        parentColumns = arrayOf("episodeId"),
        childColumns = arrayOf("episodeId"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [
        Index(value = ["seriesId"]),
        Index(value = ["episodeId"]),
    ]
)
data class UserWatchtimeDbEntry(
    @PrimaryKey(autoGenerate = true)
    val watchtimeEntry: Int,
    val seriesId: Int,
    val episodeId: Int,
)
