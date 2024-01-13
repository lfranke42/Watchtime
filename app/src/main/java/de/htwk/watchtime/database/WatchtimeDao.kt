package de.htwk.watchtime.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import de.htwk.watchtime.data.db.EpisodeDbEntry
import de.htwk.watchtime.data.db.SeriesDbEntry
import de.htwk.watchtime.data.db.UserWatchtimeDbEntry

@Dao
interface WatchtimeDao {
    @Query("SELECT * FROM series WHERE seriesId == :seriesId")
    suspend fun getSeries(seriesId: Int): SeriesDbEntry?

    @Query("SELECT * FROM episodes WHERE episodeId == :episodeId")
    suspend fun getEpisode(episodeId: Int): EpisodeDbEntry?

    @Query("SELECT episodeId FROM userwatchtime WHERE seriesId == :seriesId")
    suspend fun getWatchedEpisodeIds(seriesId: Int): List<Int>

    @Insert
    suspend fun insertSeries(vararg series: SeriesDbEntry)

    @Insert
    suspend fun insertEpisode(vararg episode: EpisodeDbEntry)

    @Insert
    suspend fun insertWatchtimeEntry(vararg watchtimeEntry: UserWatchtimeDbEntry)

    @Query("DELETE FROM userWatchtime WHERE seriesId == :seriesId AND episodeId == :episodeId ")
    suspend fun deleteWatchtimeEntry(seriesId: Int, episodeId: Int)

    @Query("UPDATE series SET seriesCompleted = :completed WHERE seriesId == :seriesId")
    suspend fun updateSeriesCompletion(seriesId: Int, completed: Boolean)
}
