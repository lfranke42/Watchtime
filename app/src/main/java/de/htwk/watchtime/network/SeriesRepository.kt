package de.htwk.watchtime.network

import de.htwk.watchtime.data.Episode
import de.htwk.watchtime.data.ExtendedSeries
import de.htwk.watchtime.data.Genre
import de.htwk.watchtime.data.Season
import de.htwk.watchtime.data.Series

interface SeriesRepository {
    suspend fun getSeries(): List<Series>
    suspend fun getSeriesDetails(id: Int): ExtendedSeries
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
                )
            )
        }
        return seriesList
    }

    override suspend fun getSeriesDetails(id: Int): ExtendedSeries {
        val seriesDetails = dataSource.getSeriesDetails(id)
        val episodeList = mutableListOf<Episode>()
        val seasonList = mutableMapOf<Int, Season>()
        val genreList = mutableListOf<Genre>()

        if (seriesDetails == null) {
            throw Exception("Series details not found")
        }

        seriesDetails.seasons.forEach { seasonDto ->
            seasonList[seasonDto.seasonNumber] = (
                    Season(
                        id = seasonDto.id,
                        seasonNumber = seasonDto.seasonNumber,
                        episodeIds = mutableListOf(),
                    )
                    )
        }

        seriesDetails.episodes.forEach { episodeDto ->
            episodeList.add(
                Episode(
                    id = episodeDto.id,
                    name = episodeDto.name,
                    seasonNumber = episodeDto.seasonNumber,
                    episodeNumber = episodeDto.episodeNumber,
                    runtime = episodeDto.runtime ?: 0,
                )
            )

            seasonList[episodeDto.seasonNumber]?.episodeIds?.add(episodeDto.id)
        }

        seriesDetails.genres?.forEach { genreDto ->
            genreList.add(
                Genre(
                    id = genreDto.id,
                    name = genreDto.name,
                )
            )
        }

        val filteredSeasonList =
            seasonList.filterKeys { seasonList[it]?.episodeIds?.isNotEmpty() ?: false }

        return ExtendedSeries(
            name = seriesDetails.name,
            id = seriesDetails.id,
            year = seriesDetails.year?.substring(0, 4) ?: "unknown",
            imageUrl = seriesDetails.imageUrl,
            episodes = episodeList,
            seasons = filteredSeasonList.toSortedMap(),
            description = seriesDetails.description,
            genres = genreList,
        )
    }
}