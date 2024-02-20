package de.htwk.watchtime.network.series

import de.htwk.watchtime.data.*

interface SeriesRepository {
    suspend fun getSeries(): List<Series>
    suspend fun getSeriesDetails(id: Int): ExtendedSeries

    suspend fun searchSeries(name: String): List<Series>
}

class SeriesRepositoryImpl(
    private val dataSource: RemoteSeriesDataSource
) : SeriesRepository {

    override suspend fun getSeries(): List<Series> {
        val seriesDtoList = dataSource.getSeries()
        val seriesList = mutableListOf<Series>()

        seriesDtoList.forEach { seriesDto ->

            val imageUrlContainsPrefix = seriesDto.imageUrl?.contains("https://artworks.thetvdb.com") ?: true

            val imageUrl = if (!imageUrlContainsPrefix) {
                seriesDto.imageUrl?.let { "https://artworks.thetvdb.com$it" } ?: ""
            } else {
                seriesDto.imageUrl ?: ""
            }

            seriesList.add(
                Series(
                    name = seriesDto.name,
                    year = seriesDto.year?.substring(0, 4) ?: "unknown",
                    imageUrl = imageUrl,
                    id = seriesDto.id,
                )
            )
        }
        return seriesList
    }

    override suspend fun searchSeries(name: String): List<Series> {
        val seriesDtoList = dataSource.searchSeries(name)
        val seriesList = mutableListOf<Series>()

        seriesDtoList.forEach { seriesDto ->
            if (seriesDto.type == "series") {
                seriesList.add(
                    Series(
                        name = seriesDto.name,
                        year = seriesDto.year?.substring(0, 4) ?: "unknown",
                        imageUrl = seriesDto.imageUrl,
                        id = seriesDto.id,
                    )
                )
            }

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
            seasonList[seasonDto.seasonNumber] = (Season(
                id = seasonDto.id,
                seasonNumber = seasonDto.seasonNumber,
                episodeIds = mutableListOf(),
            ))
        }

        seriesDetails.episodes.forEach { episodeDto ->
            episodeList.add(
                Episode(
                    id = episodeDto.id,
                    name = episodeDto.name ?: "unknown",
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

        val filteredSeasonList = seasonList.filterKeys { seasonList[it]?.episodeIds?.isNotEmpty() ?: false }

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