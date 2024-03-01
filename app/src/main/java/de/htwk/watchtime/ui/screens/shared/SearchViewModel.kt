package de.htwk.watchtime.ui.screens.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.htwk.watchtime.data.Event
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
    private val _isSearching = MutableStateFlow(false)
    private val _searchResult = MutableStateFlow<List<Series>>(emptyList())
    private val errorOccurred = MutableLiveData<Event<String>>()

    val searchQuery: StateFlow<String> get() = _searchQuery
    val isSearching: StateFlow<Boolean> get() = _isSearching
    val searchResult: StateFlow<List<Series>> get() = _searchResult
    val message: LiveData<Event<String>> get() = errorOccurred

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
                        if (series.id == null) return@forEach
                        val dbSeriesResult = watchtimeRepository.getSeries(series.id)
                        if (dbSeriesResult == null) {
                            watchtimeRepository.insertSeries(series)
                        }
                    }
                } catch (e: Exception) {
                    errorOccurred.value =
                        Event("Failed to search the database, check your internet connection")
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


