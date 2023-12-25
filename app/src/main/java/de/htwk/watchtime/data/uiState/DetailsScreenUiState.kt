package de.htwk.watchtime.data.uiState

import de.htwk.watchtime.data.ExtendedSeries

data class DetailsScreenUiState(
    val seriesDetails: ExtendedSeries,
    val selectedSeason: Int,
    val bottomSheetVisible: Boolean = false,
    val episodesWatched: Set<Int> = emptySet(),
    val seasonCompleted: Boolean = false,
    val dropDownExpanded: Boolean = false,
    val seriesCompleted: Boolean = false,
)

