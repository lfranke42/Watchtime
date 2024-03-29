package de.htwk.watchtime.ui.screens.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.htwk.watchtime.data.Event
import de.htwk.watchtime.data.Series
import de.htwk.watchtime.data.placeholder.placeHolderSeriesList
import de.htwk.watchtime.data.toSeries
import de.htwk.watchtime.data.uiState.HomeScreenUiState
import de.htwk.watchtime.database.WatchtimeRepository
import de.htwk.watchtime.network.series.SeriesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val seriesRepository: SeriesRepository,
    private val watchtimeRepository: WatchtimeRepository,
) : ViewModel() {

    private val errorOccurred = MutableLiveData<Event<String>>()
    private val _homeScreenUiState: MutableStateFlow<HomeScreenUiState> = MutableStateFlow(
        HomeScreenUiState(
            series = placeHolderSeriesList,
            continueWatchingList = placeHolderSeriesList
        )
    )
    val uiState: StateFlow<HomeScreenUiState> = _homeScreenUiState.asStateFlow()
    val message: LiveData<Event<String>> get() = errorOccurred

    init {
        loadSeries()
        updateContinueWatchingList()
    }

    private fun loadSeries() {
        viewModelScope.launch {
            val fetchedSeries: List<Series>
            try {
                fetchedSeries = seriesRepository.getSeries()
            } catch (e: Exception) {
                errorOccurred.value = Event( "Error occurred while retrieving series")
                return@launch
            }

            _homeScreenUiState.update { currentState ->
                currentState.copy(
                    series = fetchedSeries
                )
            }

            // Check if series already in DB, if not add it
            uiState.value.series.forEach { series ->
                if (series.id == null) return@forEach
                val dbSeriesResult = watchtimeRepository.getSeries(series.id)
                if (dbSeriesResult == null) {
                    watchtimeRepository.insertSeries(series)
                }
            }
        }
    }

    fun updateContinueWatchingList() {
        viewModelScope.launch {
            val startedSeriesIds = watchtimeRepository.getStartedSeriesIds()
            val startedSeries = mutableListOf<Series>()

            startedSeriesIds.forEach { seriesId ->
                try {
                    startedSeries.add(seriesRepository.getSeriesDetails(seriesId).toSeries())
                } catch (e: Exception) {
                    errorOccurred.value =
                        Event("Error occurred while retrieving series details")
                    return@launch
                }
            }

            _homeScreenUiState.update { currentState ->
                currentState.copy(
                    continueWatchingList = startedSeries
                )
            }
        }
    }
}