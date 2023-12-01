package de.htwk.watchtime.ui.screens.shared

import androidx.lifecycle.ViewModel
import de.htwk.watchtime.network.SeriesRepository

class DetailsViewModel(
    private val seriesRepository: SeriesRepository
): ViewModel() {
}