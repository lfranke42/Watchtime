package de.htwk.watchtime.ui.screens.shared

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.htwk.watchtime.data.ExtendedSeries
import de.htwk.watchtime.data.uiState.DetailsScreenUiState
import de.htwk.watchtime.network.SeriesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val seriesRepository: SeriesRepository
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
        }
    }

    fun selectSeason(season: Int) {
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
            /* TODO: DB update */
        } else {
            _detailsScreenUiState.update { currentState ->
                currentState.copy(
                    episodesWatched = currentState.episodesWatched - episodeId
                )
            }
            /* TODO: DB update */
        }
    }
}