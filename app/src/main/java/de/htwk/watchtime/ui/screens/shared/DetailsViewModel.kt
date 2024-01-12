package de.htwk.watchtime.ui.screens.shared

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.htwk.watchtime.data.ExtendedSeries
import de.htwk.watchtime.data.Season
import de.htwk.watchtime.data.uiState.DetailsScreenUiState
import de.htwk.watchtime.database.WatchtimeRepository
import de.htwk.watchtime.network.SeriesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val seriesRepository: SeriesRepository,
    private val watchtimeRepository: WatchtimeRepository,
) : ViewModel() {
    private val seriesId: Int = checkNotNull(savedStateHandle.get<Int>("seriesId"))

    private val _detailsScreenUiState: MutableStateFlow<DetailsScreenUiState> = MutableStateFlow(
        DetailsScreenUiState(
            seriesDetails = ExtendedSeries(
                name = "Loading...",
                id = 0,
                year = "Loading...",
                imageUrl = null,
                episodes = emptyList(),
                seasons = emptyMap(),
                description = null,
                genres = emptyList()
            ), selectedSeason = 1
        )
    )
    val seriesDetails: StateFlow<DetailsScreenUiState> = _detailsScreenUiState

    init {
        loadSeriesDetails()
    }

    private fun loadSeriesDetails() {
        viewModelScope.launch {
            val seriesDetails = seriesRepository.getSeriesDetails(seriesId)

            _detailsScreenUiState.update { currentState ->
                currentState.copy(
                    seriesDetails = seriesDetails,
                    selectedSeason = seriesDetails.seasons.keys.first()
                )
            }

            // Check if episodes already in DB, if not add them
            seriesDetails.episodes.forEach { episode ->
                val dbEpisodeResult = watchtimeRepository.getEpisode(episode.id)
                if (dbEpisodeResult == null) {
                    watchtimeRepository.insertNewEpisode(episode, seriesId)
                }
            }
        }
    }

    fun selectSeason(season: Int) {
        /* TODO: fetch season watch status from DB and update checkbox accordingly */
        _detailsScreenUiState.update { currentState ->
            currentState.copy(selectedSeason = season, bottomSheetVisible = false)
        }
    }

    fun openBottomSheet() {
        _detailsScreenUiState.update { currentState ->
            currentState.copy(bottomSheetVisible = true)
        }
    }

    fun closeBottomSheet() {
        _detailsScreenUiState.update { currentState ->
            currentState.copy(bottomSheetVisible = false)
        }
    }

    fun toggleEpisodeWatched(episodeId: Int, watched: Boolean) {
        if (watched) {
            _detailsScreenUiState.update { currentState ->
                currentState.copy(
                    episodesWatched = currentState.episodesWatched + episodeId
                )
            }
            Log.i("DetailsViewModel", "Episode $episodeId watched")

            val seasonNumber = seriesDetails.value.seriesDetails.episodes.find { it.id == episodeId }
            Log.i("DetailsViewModel", "Belonging to season number $seasonNumber")
            /* TODO: DB update */
        } else {
            _detailsScreenUiState.update { currentState ->
                currentState.copy(
                    episodesWatched = currentState.episodesWatched - episodeId
                )
            }
            /* TODO: DB update */
        }
        /* TODO: Check in DB if whole season is watched */
    }

    fun toggleSeasonWatched(season: Season, completed: Boolean) {
        if (!completed) {
            _detailsScreenUiState.update { currentState ->
                val seriesEpisodes =
                    currentState.seriesDetails.episodes.filter { it.seasonNumber == season.seasonNumber }
                val episodeIds = seriesEpisodes.map { it.id }

                currentState.copy(
                    episodesWatched = currentState.episodesWatched + episodeIds.toSet(),
                    seasonCompleted = true
                )

            }
            Log.i("check", "season: ${season.seasonNumber}")
            Log.i("check", "completed: ${seriesDetails.value.episodesWatched}")
            /* TODO: DB update */
        } else {
            _detailsScreenUiState.update { currentState ->
                val seriesEpisodes =
                    currentState.seriesDetails.episodes.filter { it.seasonNumber == season.seasonNumber }
                val episodeIds = seriesEpisodes.map { it.id }

                currentState.copy(
                    episodesWatched = currentState.episodesWatched - episodeIds.toSet(),
                    seasonCompleted = false
                )
            }
            /* TODO: DB update */
        }
    }

    fun toggleDropDown() {
        _detailsScreenUiState.update { currentState ->
            currentState.copy(
                dropDownExpanded = !currentState.dropDownExpanded
            )
        }
    }

    fun dismissDropDown() {
        _detailsScreenUiState.update { currentState ->
            currentState.copy(
                dropDownExpanded = false
            )
        }
    }

    fun toggleSeriesWatched() {
        val seriesCompleted = seriesDetails.value.seriesCompleted

        if (seriesCompleted) {
            _detailsScreenUiState.update { currentState ->
                currentState.copy(
                    episodesWatched = setOf(),
                    seriesCompleted = false
                )
            }
            /* TODO: DB update */
            return
        }

        val episodeList = seriesDetails.value.seriesDetails.episodes
        val episodeIdList = episodeList.map { it.id }

        _detailsScreenUiState.update { currentState ->
            currentState.copy(
                episodesWatched = episodeIdList.toSet(),
                seriesCompleted = true
            )
        }
        /* TODO: DB update */
    }
}