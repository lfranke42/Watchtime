package de.htwk.watchtime.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.htwk.watchtime.data.Series
import de.htwk.watchtime.ui.screens.shared.SearchViewModel
import de.htwk.watchtime.ui.screens.shared.components.RoundedSearchBar
import de.htwk.watchtime.ui.screens.shared.components.SeriesCard
import org.koin.androidx.compose.getViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onCardTap: (seriesId: Int) -> Unit,
    viewModel: SearchViewModel = getViewModel()
) {
    SearchScreenContent(
        modifier = modifier,
        onCardTap = onCardTap,
        viewModel = viewModel
    )
}

@Composable
fun SearchScreenContent(
    modifier: Modifier = Modifier,
    onCardTap: (seriesId: Int) -> Unit,
    viewModel: SearchViewModel
) {
    val searchResult: List<Series> by viewModel.searchResult.collectAsState()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            RoundedSearchBar(viewModel = viewModel, onCardTap = onCardTap)
        }
        searchResult.forEach { series ->
            item {
                SeriesCard(series = series, onTap = onCardTap)
            }
        }
    }
}


