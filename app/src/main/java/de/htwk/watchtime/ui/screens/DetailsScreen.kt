package de.htwk.watchtime.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.htwk.watchtime.R
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
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = title, style = MaterialTheme.typography.titleMedium,
                    maxLines = 1, overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = year,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Light
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "$seasons Seasons - $episodes Episodes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Light
                )
            }
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = stringResource(id = R.string.details_screen_more_options)
            )
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