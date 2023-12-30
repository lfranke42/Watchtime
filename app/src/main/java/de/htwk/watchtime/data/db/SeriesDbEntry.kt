package de.htwk.watchtime.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "series")
data class SeriesDbEntry(
    @PrimaryKey
    val seriesId: Int,
    val seasonIdsCompleted: Set<Int>,
    val seriesCompleted: Boolean,
)
