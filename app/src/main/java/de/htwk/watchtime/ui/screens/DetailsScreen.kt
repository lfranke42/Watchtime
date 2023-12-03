package de.htwk.watchtime.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import de.htwk.watchtime.data.ExtendedSeries
import de.htwk.watchtime.ui.screens.shared.DetailsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel = koinViewModel()
) {
    val seriesDetails by viewModel.seriesDetails.collectAsState()
    DetailsScreen(seriesDetails = seriesDetails, modifier = modifier)
}

@Composable
fun DetailsScreen(
    seriesDetails: ExtendedSeries,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        CardHeader(
            title = seriesDetails.name,
            year = seriesDetails.year,
            seasons = seriesDetails.seasons.size.toString(),
            episodes = seriesDetails.episodes.size.toString()
        )
    }
}

@Composable
fun CardHeader(title: String, year: String, seasons: String, episodes: String) {
    Row {
        Column {
            Row {
                Text(text = title)
                Text(text = year)
            }
        }
    }
}

@Preview
@Composable
fun DetailsScreenPreview() {
    Surface {
        DetailsScreen()
    }
}