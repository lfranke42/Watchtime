package de.htwk.watchtime.database

import android.icu.util.Calendar
import de.htwk.watchtime.data.Episode
import de.htwk.watchtime.data.Series
import de.htwk.watchtime.data.db.EpisodeDbEntry
import de.htwk.watchtime.data.db.SeriesDbEntry
import de.htwk.watchtime.data.db.UserWatchtimeDbEntry

interface WatchtimeRepository {
    suspend fun getSeries(seriesId: Int): SeriesDbEntry?
    suspend fun getEpisode(episodeId: Int): EpisodeDbEntry?
    suspend fun getWatchedEpisodeIds(seriesId: Int): List<Int>
    suspend fun insertNewEpisode(episode: Episode, seriesId: Int)
    suspend fun insertSeries(series: Series)
    suspend fun insertWatchtimeEntry(seriesId: Int, episodeId: Int)
    suspend fun deleteWatchtimeEntry(watchtimeEntry: UserWatchtimeDbEntry)

}

class WatchtimeRepositoryImpl(private val watchtimeDataSource: LocalWatchtimeDataSource) :
    WatchtimeRepository {

    override suspend fun getSeries(seriesId: Int): SeriesDbEntry? {
        return watchtimeDataSource.getSeries(seriesId)
    }

    override suspend fun getEpisode(episodeId: Int): EpisodeDbEntry? {
        return watchtimeDataSource.getEpisode(episodeId)
    }

    override suspend fun getWatchedEpisodeIds(seriesId: Int): List<Int> {
        return watchtimeDataSource.getWatchedEpisodeIds(seriesId)
    }

    override suspend fun insertNewEpisode(episode: Episode, seriesId: Int) {
        val dbEpisode = EpisodeDbEntry(
            episodeId = episode.id,
            seriesId = seriesId,
            seasonNumber = episode.seasonNumber,
            runtime = episode.runtime,
        )
        watchtimeDataSource.insertNewEpisode(dbEpisode)
    }

    override suspend fun insertSeries(series: Series) {
        val dbSeries = SeriesDbEntry(
            seriesId = series.id,
            seriesCompleted = false
        )
        watchtimeDataSource.insertSeries(dbSeries)
    }

    override suspend fun insertWatchtimeEntry(seriesId: Int, episodeId: Int) {
        val watchtimeEntry = UserWatchtimeDbEntry(
            seriesId = seriesId,
            episodeId = episodeId,
            dateWatched = Calendar.getInstance().time
        )
        watchtimeDataSource.insertWatchtimeEntry(watchtimeEntry)
    }

    override suspend fun deleteWatchtimeEntry(watchtimeEntry: UserWatchtimeDbEntry) {
        watchtimeDataSource.deleteWatchtimeEntry(watchtimeEntry)
    }
}