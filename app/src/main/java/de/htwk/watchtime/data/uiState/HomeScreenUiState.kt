package de.htwk.watchtime.data.uiState

import de.htwk.watchtime.data.Series

data class HomeScreenUiState(
    val series: List<Series>,
    val continueWatchingList: List<Series>
)