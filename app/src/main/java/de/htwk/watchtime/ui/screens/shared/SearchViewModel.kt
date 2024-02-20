package de.htwk.watchtime.ui.screens.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.htwk.watchtime.data.Series
import de.htwk.watchtime.database.WatchtimeRepository
import de.htwk.watchtime.network.series.SeriesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val seriesRepository: SeriesRepository,
    private val watchtimeRepository: WatchtimeRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> get() = _isSearching

    private val _searchResult = MutableStateFlow<List<Series>>(emptyList())
    val searchResult: StateFlow<List<Series>> get() = _searchResult

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun performSearch() {
        val currentQuery = _searchQuery.value
        if (currentQuery.isNotBlank()) {
            viewModelScope.launch {
                _isSearching.value = true
                try {
                    val seriesList = seriesRepository.searchSeries(currentQuery)
                    _searchResult.value = seriesList.map { series ->
                        Series(
                            name = series.name,
                            id = series.id,
                            year = series.year,
                            imageUrl = series.imageUrl
                        )
                    }
                    // Check if series already in DB, if not add it
                    _searchResult.value.forEach { series ->
                        val dbSeriesResult = watchtimeRepository.getSeries(series.id)
                        if (dbSeriesResult == null) {
                            watchtimeRepository.insertSeries(series)
                        }
                    }
                } finally {
                    _isSearching.value = false
                }
            }
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _isSearching.value = false
        _searchResult.value = emptyList()
    }
}


