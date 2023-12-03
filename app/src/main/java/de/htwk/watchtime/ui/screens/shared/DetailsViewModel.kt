package de.htwk.watchtime.ui.screens.shared

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.htwk.watchtime.data.ExtendedSeries
import de.htwk.watchtime.network.SeriesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val seriesRepository: SeriesRepository
): ViewModel() {
    private val seriesId: Int = checkNotNull(savedStateHandle.get<Int>("seriesId"))
    private val _seriesDetails: MutableStateFlow<ExtendedSeries> = MutableStateFlow(ExtendedSeries("", 0, "", "", emptyList(), emptyMap(), ""))
    val seriesDetails: StateFlow<ExtendedSeries> = _seriesDetails

    init {
        loadSeriesDetails()
    }

    private fun loadSeriesDetails() {
        viewModelScope.launch {
            _seriesDetails.update {
                seriesRepository.getSeriesDetails(seriesId)
            }
        }
    }
}