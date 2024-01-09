package de.htwk.watchtime.ui.screens.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.htwk.watchtime.data.Series
import de.htwk.watchtime.data.placeholder.placeHolderSeriesList
import de.htwk.watchtime.network.SeriesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val seriesRepository: SeriesRepository
) : ViewModel() {


    private val _series: MutableStateFlow<List<Series>> = MutableStateFlow(placeHolderSeriesList)
    val series: StateFlow<List<Series>> = _series.asStateFlow()


    init {
        loadSeries()
    }

    private fun loadSeries() {
        viewModelScope.launch {
            _series.update {
                seriesRepository.getSeries()
            }
        }
    }





}
