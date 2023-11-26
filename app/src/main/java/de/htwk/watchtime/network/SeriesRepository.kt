package de.htwk.watchtime.network

import de.htwk.watchtime.data.Series

interface SeriesRepository {
    suspend fun getSeries(): List<Series>
}

class SeriesRepositoryImpl(
    private val dataSource: RemoteSeriesDataSource
) : SeriesRepository {

    override suspend fun getSeries(): List<Series> {
        val seriesDtoList = dataSource.getSeries()
        val seriesList = mutableListOf<Series>()

        seriesDtoList.forEach { seriesDto ->
            seriesList.add( Series(
                name = seriesDto.name,
                year = seriesDto.year?.substring(0, 4) ?: "unknown",
                imageUrl = seriesDto.imageUrl
            ) )
        }

        return seriesList
    }

}