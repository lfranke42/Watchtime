package de.htwk.watchtime.data.uiState

import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import de.htwk.watchtime.data.Ranking

data class StatsScreenUiState(
    val totalWatchtime: String,
    val leaderboard: Ranking? = null,
    val chartEntryModelProducer: ChartEntryModelProducer? = null,
    val personalRank: Int? = null
)
