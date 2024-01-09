package de.htwk.watchtime.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.htwk.watchtime.data.Series
import de.htwk.watchtime.ui.screens.shared.SearchViewModel
import de.htwk.watchtime.ui.screens.shared.components.RoundedSearchBar
import de.htwk.watchtime.ui.screens.shared.components.SeriesCard
import org.koin.androidx.compose.koinViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onCardTap: (seriesId: Int) -> Unit,
    viewModel: SearchViewModel = koinViewModel()
) {
    val seriesList by viewModel.series.collectAsState()
    SearchScreen(
        modifier = modifier,
        mostFrequentlySearch = seriesList,
        onCardTap = onCardTap
    )
    
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    mostFrequentlySearch: List<Series>,
    onCardTap: (seriesId: Int) -> Unit = {}
){

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        item {
            RoundedSearchBar(seriesList =
                mostFrequentlySearch, onCardTap = onCardTap
            )
        }
        item {
            Spacer(modifier = Modifier.height(100.dp))
            MostFrequentlySearchTitle(text = "Most frequently searched")
        }
        items(mostFrequentlySearch) { series ->
            SeriesCard(series = series, onTap = onCardTap)

        }

    }
}

@Composable
fun MostFrequentlySearchTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        style = MaterialTheme.typography.titleLarge
    )
}
@Preview
@Composable
fun SearchScreenPreview() {
    Surface {
        SearchScreen(
            mostFrequentlySearch = List(1) {
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