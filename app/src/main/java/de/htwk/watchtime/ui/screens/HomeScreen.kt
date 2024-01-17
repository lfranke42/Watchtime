package de.htwk.watchtime.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.htwk.watchtime.R
import de.htwk.watchtime.data.Series
import de.htwk.watchtime.ui.screens.shared.HomeViewModel
import de.htwk.watchtime.ui.screens.shared.components.CarouselCard
import de.htwk.watchtime.ui.screens.shared.components.SeriesCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onCardTap: (seriesId: Int) -> Unit = {},
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    HomeScreen(
        recommendedSeries = uiState.series,
        continueWatchingList = uiState.continueWatchingList,
        onCardTap = onCardTap,
        refreshContinueWatching = { viewModel.updateContinueWatchingList() },
        modifier = modifier
    )
}

@Composable
fun HomeScreen(
    recommendedSeries: List<Series>,
    continueWatchingList: List<Series>,
    modifier: Modifier = Modifier,
    onCardTap: (seriesId: Int) -> Unit = {},
    refreshContinueWatching: () -> Unit = {}
) {
    LaunchedEffect(Unit){
        refreshContinueWatching()
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (continueWatchingList.isNotEmpty()) {
            item {
                HomeScreenTitle(text = stringResource(id = R.string.home_screen_continue_watching_title))
            }
            item {
                ContinueWatchingCarousel(
                    continueWatchingList = continueWatchingList,
                    onCardTap = onCardTap
                )
            }
        }
        item {
            HomeScreenTitle(text = stringResource(id = R.string.home_screen_recommended_title))
        }
        items(recommendedSeries) { series ->
            SeriesCard(series = series, onTap = onCardTap)
        }
    }

}

@Composable
fun HomeScreenTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
fun ContinueWatchingCarousel(
    modifier: Modifier = Modifier,
    continueWatchingList: List<Series>,
    onCardTap: (seriesId: Int) -> Unit = {}
) {
    LazyRow(
        modifier = modifier.fillMaxWidth().padding(start = 2.dp, end = 2.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(continueWatchingList) { series ->
            CarouselCard(series, onCardTap = onCardTap)
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    Surface {
        HomeScreen(
            recommendedSeries = List(10) {
                Series(
                    name = "Breaking Bad",
                    year = "2013",
                    imageUrl = "/img/asdf399",
                    id = 1
                )
            }, continueWatchingList = List(10) {
                Series(
                    name = "Breaking Bad",
                    year = "2013",
                    imageUrl = "/img/asdf399",
                    id = 1
                )
            }
        )
    }
}