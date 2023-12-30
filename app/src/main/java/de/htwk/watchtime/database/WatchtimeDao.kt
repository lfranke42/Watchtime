package de.htwk.watchtime.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import de.htwk.watchtime.data.db.SeriesDbEntry

@Dao
interface WatchtimeDao {
    @Query("SELECT * FROM series WHERE seriesId == :seriesId")
    suspend fun getSeries(seriesId: Int): SeriesDbEntry

    @Insert
    suspend fun updateSeries(vararg series: SeriesDbEntry)
}
