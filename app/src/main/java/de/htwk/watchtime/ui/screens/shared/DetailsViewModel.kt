package de.htwk.watchtime.ui.screens.shared

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
    val uiState: StateFlow<DetailsScreenUiState> = _detailsScreenUiState

    init {
        loadSeriesDetails()
    }

    private fun loadSeriesDetails() {
        viewModelScope.launch {
            val seriesDetails = seriesRepository.getSeriesDetails(seriesId)
            val episodeIdsWatched = watchtimeRepository.getWatchedEpisodeIds(seriesId)

            _detailsScreenUiState.update { currentState ->
                currentState.copy(
                    seriesDetails = seriesDetails,
                    selectedSeason = seriesDetails.seasons.keys.first(),
                    episodesWatched = episodeIdsWatched.toSet()
                )
            }

            // Check if episodes already in DB, if not add them
            seriesDetails.episodes.forEach { episode ->
                val dbEpisodeResult = watchtimeRepository.getEpisode(episode.id)
                if (dbEpisodeResult == null) {
                    watchtimeRepository.insertNewEpisode(episode, seriesId)
                }
            }

            checkIfSeasonIsCompleted()
        }
    }

    fun selectSeason(seasonNumber: Int) {
        val episodeIds =
            uiState.value.seriesDetails.seasons[seasonNumber]?.episodeIds ?: listOf()

        viewModelScope.launch {
            val watchedEpisodeIds = watchtimeRepository.getWatchedEpisodeIds(seriesId)

            episodeIds.forEach { episodeId ->
                if (!watchedEpisodeIds.contains(episodeId)) {
                    return@forEach
                }

                _detailsScreenUiState.update { currentState ->
                    currentState.copy(
                        episodesWatched = currentState.episodesWatched + episodeId
                    )
                }
            }
            checkIfSeasonIsCompleted()
        }
        _detailsScreenUiState.update { currentState ->
            currentState.copy(selectedSeason = seasonNumber, bottomSheetVisible = false)
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
            viewModelScope.launch {
                watchtimeRepository.insertWatchtimeEntry(seriesId = seriesId, episodeId = episodeId)
                checkIfSeasonIsCompleted()
            }
            return
        }

        _detailsScreenUiState.update { currentState ->
            currentState.copy(
                episodesWatched = currentState.episodesWatched - episodeId
            )
        }
        viewModelScope.launch {
            watchtimeRepository.deleteWatchtimeEntry(seriesId = seriesId, episodeId = episodeId)
            checkIfSeasonIsCompleted()
        }
    }

    fun toggleSeasonWatched(season: Season, completed: Boolean) {
        if (!completed) {
            _detailsScreenUiState.update { currentState ->
                currentState.copy(
                    episodesWatched = currentState.episodesWatched + season.episodeIds.toSet(),
                )
            }
            viewModelScope.launch {
                season.episodeIds.forEach { episodeId ->
                    watchtimeRepository.insertWatchtimeEntry(
                        seriesId = seriesId,
                        episodeId = episodeId
                    )
                }
                checkIfSeasonIsCompleted()
            }
            return
        }
        _detailsScreenUiState.update { currentState ->
            currentState.copy(
                episodesWatched = currentState.episodesWatched - season.episodeIds.toSet(),
            )
        }
        viewModelScope.launch {
            season.episodeIds.forEach { episodeId ->
                watchtimeRepository.deleteWatchtimeEntry(
                    seriesId = seriesId,
                    episodeId = episodeId
                )
            }
            checkIfSeasonIsCompleted()
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
        val seriesCompleted = uiState.value.seriesCompleted
        val episodeList = uiState.value.seriesDetails.episodes
        val episodeIdList = episodeList.map { it.id }

        if (seriesCompleted) {
            _detailsScreenUiState.update { currentState ->
                currentState.copy(
                    episodesWatched = setOf(),
                )
            }
            viewModelScope.launch {
                episodeIdList.forEach { episodeId ->
                    watchtimeRepository.deleteWatchtimeEntry(
                        seriesId = seriesId,
                        episodeId = episodeId
                    )
                }
                checkIfSeasonIsCompleted()
            }
            return
        }

        _detailsScreenUiState.update { currentState ->
            currentState.copy(
                episodesWatched = episodeIdList.toSet(),
            )
        }
        viewModelScope.launch {
            episodeIdList.forEach { episodeId ->
                watchtimeRepository.insertWatchtimeEntry(seriesId = seriesId, episodeId = episodeId)
            }
            checkIfSeasonIsCompleted()
        }
    }

    private fun checkIfSeasonIsCompleted() {
        val watchedEpisodes = uiState.value.episodesWatched
        val currentSeason = uiState.value.selectedSeason
        val episodesOfSeason =
            uiState.value.seriesDetails.seasons[currentSeason]?.episodeIds
                ?: emptyList()

        val seasonCompleted = watchedEpisodes.containsAll(episodesOfSeason)
        _detailsScreenUiState.update { currentState ->
            currentState.copy(
                seasonCompleted = seasonCompleted
            )
        }

        checkIfSeriesIsCompleted()
    }

    private fun checkIfSeriesIsCompleted() {
        viewModelScope.launch {
            val watchedEpisodes = watchtimeRepository.getWatchedEpisodeIds(seriesId)
            val allEpisodeIds = uiState.value.seriesDetails.episodes.map { it.id }
            val newCompletionState = watchedEpisodes.containsAll(allEpisodeIds)

            _detailsScreenUiState.update { currentState ->
                currentState.copy(
                    seriesCompleted = newCompletionState
                )
            }

            // TODO: Optimize, so that DB is only updated if value actually changes
            viewModelScope.launch {
                watchtimeRepository.updateSeriesCompletion(seriesId, newCompletionState)
            }
        }
    }
}