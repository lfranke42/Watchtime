package de.htwk.watchtime.database

import de.htwk.watchtime.data.db.EpisodeDbEntry
import de.htwk.watchtime.data.db.SeriesDbEntry
import de.htwk.watchtime.data.db.UserWatchtimeDbEntry
import java.util.Date

interface LocalWatchtimeDataSource {
    suspend fun getSeries(seriesId: Int): SeriesDbEntry?
    suspend fun getEpisode(episodeId: Int): EpisodeDbEntry?
    suspend fun getWatchedEpisodeIds(seriesId: Int): List<Int>
    suspend fun insertSeries(series: SeriesDbEntry)
    suspend fun insertNewEpisode(episode: EpisodeDbEntry)
    suspend fun insertWatchtimeEntry(watchtimeEntry: UserWatchtimeDbEntry)
    suspend fun deleteWatchtimeEntry(watchtimeEntry: UserWatchtimeDbEntry)
    suspend fun modifyWatchtimeDate(episodeId: Int, dateWatched: Date)

}

class LocalWatchtimeDataSourceImpl(private val watchtimeDao: WatchtimeDao) :
    LocalWatchtimeDataSource {
    override suspend fun getSeries(seriesId: Int): SeriesDbEntry? {
        return watchtimeDao.getSeries(seriesId)
    }

    override suspend fun getEpisode(episodeId: Int): EpisodeDbEntry? {
        return watchtimeDao.getEpisode(episodeId)
    }

    override suspend fun getWatchedEpisodeIds(seriesId: Int): List<Int> {
        return watchtimeDao.getWatchedEpisodeIds(seriesId)
    }

    override suspend fun insertNewEpisode(episode: EpisodeDbEntry) {
        watchtimeDao.insertEpisode(episode)
    }

    override suspend fun insertSeries(series: SeriesDbEntry) {
        watchtimeDao.insertSeries(series)
    }

    override suspend fun insertWatchtimeEntry(watchtimeEntry: UserWatchtimeDbEntry) {
        watchtimeDao.insertWatchtimeEntry(watchtimeEntry)
    }

    override suspend fun deleteWatchtimeEntry(watchtimeEntry: UserWatchtimeDbEntry) {
        watchtimeDao.deleteWatchtimeEntry(watchtimeEntry)
    }

    override suspend fun modifyWatchtimeDate(episodeId: Int, dateWatched: Date) {
        TODO("Not yet implemented")
    }
}

