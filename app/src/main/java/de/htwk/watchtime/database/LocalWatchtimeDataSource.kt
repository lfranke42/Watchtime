package de.htwk.watchtime.database

import de.htwk.watchtime.data.db.SeriesDbEntry

interface LocalWatchtimeDataSource {
    suspend fun getSeriesWatchtime(seriesId: Int): SeriesDbEntry

}

class LocalWatchtimeDataSourceImpl(private val watchtimeDao: WatchtimeDao): LocalWatchtimeDataSource {
    override suspend fun getSeriesWatchtime(seriesId: Int): SeriesDbEntry {
        return watchtimeDao.getSeries(seriesId)
    }
}

