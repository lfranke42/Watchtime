package de.htwk.watchtime.ui.screens.shared

import android.content.res.Resources.NotFoundException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf
import de.htwk.watchtime.data.Event
import de.htwk.watchtime.data.Ranking
import de.htwk.watchtime.data.uiState.StatsScreenUiState
import de.htwk.watchtime.database.WatchtimeRepository
import de.htwk.watchtime.network.ranking.RankingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class StatsViewModel(
    private val watchtimeRepository: WatchtimeRepository,
    private val rankingRepository: RankingRepository
) : ViewModel() {
    private val errorOccurred = MutableLiveData<Event<String>>()
    private val _uiState = MutableStateFlow(
        StatsScreenUiState(
            totalWatchtime = ""
        )
    )
    val uiState: StateFlow<StatsScreenUiState> = _uiState.asStateFlow()
    val message: LiveData<Event<String>> get() = errorOccurred

    init {
        updateTotalWatchtime()
        fetchRanking()
    }

    fun updateTotalWatchtime() {
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

    private fun fetchRanking() {
        viewModelScope.launch {
            val ranking: Ranking
            try {
                ranking = rankingRepository.getRanking()
            } catch (e: NotFoundException) {
                _uiState.value = _uiState.value.copy(
                    noTimeTracked = true
                )
                return@launch
            } catch (e: Exception) {
                errorOccurred.value = Event("Failed to retrieve ranking")
                return@launch
            }


            _uiState.value = _uiState.value.copy(
                leaderboard = ranking,
                noTimeTracked = false
            )

            val chartEntryList =
                ranking.closestNeighbors.map {
                    entryOf(
                        it.position,
                        it.totalWatchtime.toFloat() / 60
                    )
                }.toMutableList()
            chartEntryList.add(entryOf(ranking.position, ranking.totalWatchtime.toFloat() / 60))

            val chartEntryModelProducer = ChartEntryModelProducer(chartEntryList)

            _uiState.value = _uiState.value.copy(
                personalRank = ranking.position,
                chartEntryModelProducer = chartEntryModelProducer
            )
        }
    }
}