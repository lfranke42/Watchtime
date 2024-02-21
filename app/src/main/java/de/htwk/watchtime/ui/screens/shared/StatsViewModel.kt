package de.htwk.watchtime.ui.screens.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.htwk.watchtime.data.uiState.StatsScreenUiState
import de.htwk.watchtime.database.WatchtimeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StatsViewModel(private val watchtimeRepository: WatchtimeRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(
        StatsScreenUiState(
            totalWatchtime = ""
        )
    )
    val uiState: StateFlow<StatsScreenUiState> = _uiState.asStateFlow()

    init {
        updateTotalWatchtime()
    }

    private fun updateTotalWatchtime() {
        viewModelScope.launch {
            var watchtimeInMinutes = watchtimeRepository.getTotalWatchtime()
            var watchtimeInHours = 0
            var watchtimeInDays = 0
            while (watchtimeInMinutes > 60) {
                watchtimeInMinutes -= 60
                watchtimeInHours++
            }
            while (watchtimeInHours > 24) {
                watchtimeInHours -= 24
                watchtimeInDays++
            }

            val watchtimeString: String = if (watchtimeInDays > 0) {
                "${watchtimeInDays}d ${watchtimeInHours}h ${watchtimeInMinutes}m"
            } else if (watchtimeInHours > 0) {
                "${watchtimeInHours}h ${watchtimeInMinutes}m"
            } else {
                "${watchtimeInMinutes}m"
            }

            _uiState.value = _uiState.value.copy(
                totalWatchtime = watchtimeString
            )
        }
    }
}