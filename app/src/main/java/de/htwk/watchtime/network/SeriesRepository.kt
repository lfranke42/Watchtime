package de.htwk.watchtime.network

import de.htwk.watchtime.data.Episode
import de.htwk.watchtime.data.Series

interface SeriesRepository {
    suspend fun getSeries(): List<Series>
    suspend fun getEpisodes(): List<Episode>
}

class SeriesRepositoryImpl(
    private val dataSource: RemoteSeriesDataSource
) : SeriesRepository {

    override suspend fun getSeries(): List<Series> {
        val seriesDtoList = dataSource.getSeries()
        val seriesList = mutableListOf<Series>()

        seriesDtoList.forEach { seriesDto ->
            seriesList.add(
                Series(
                    name = seriesDto.name,
                    year = seriesDto.year?.substring(0, 4) ?: "unknown",
                    imageUrl = seriesDto.imageUrl,
                    id = seriesDto.id,
                    episodes = null
                )
            )
        }
        return seriesList
    }

    override suspend fun getEpisodes(): List<Episode> {
        val episodeDtoList = dataSource.getEpisodes()
        val episodeList = mutableListOf<Episode>()

        episodeDtoList.forEach { episodeDto ->
            episodeList.add(
                Episode(
                    id = episodeDto.id,
                    name = episodeDto.name,
                    seasonNumber = episodeDto.seasonNumber,
                    episodeNumber = episodeDto.episodeNumber,
                    runtime = episodeDto.runtime
                )
            )
        }
        return episodeList
    }
}