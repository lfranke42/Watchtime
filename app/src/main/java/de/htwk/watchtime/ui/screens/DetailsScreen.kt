package de.htwk.watchtime.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
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
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),

        ) {
        LazyColumn {
            item {
                CardHeader(
                    title = seriesDetails.name,
                    year = seriesDetails.year,
                    seasons = seriesDetails.seasons.size.toString(),
                    episodes = seriesDetails.episodes.size.toString()
                )
            }
            item {
                SeriesImage(
                    imageUrl = seriesDetails.imageUrl ?: "unavailable",
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
            if (seriesDetails.description != null) {
                item {
                    SeriesDescription(description = seriesDetails.description)
                }
            }
        }
    }
}

@Composable
fun CardHeader(title: String, year: String, seasons: String, episodes: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title, style = MaterialTheme.typography.titleMedium,
                maxLines = 1, overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$seasons Seasons - $episodes Episodes - $year ",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Light,
                maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                Icons.Default.MoreVert,
                contentDescription = stringResource(id = R.string.details_screen_more_options)
            )
        }
    }
}

@Composable
fun SeriesImage(imageUrl: String, modifier: Modifier = Modifier) {
    var surfaceHeight by remember { mutableStateOf(350.dp) }

    Surface(
        modifier = modifier.height(surfaceHeight),
        color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = stringResource(id = R.string.details_screen_image_description),
            fallback = null,
            error = null,
            onError = {
                if (imageUrl.isNotEmpty()) surfaceHeight = 0.dp
            },
            onSuccess = {
                surfaceHeight = 350.dp
                Log.i("Success Image", "called")
            },
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
fun SeriesDescription(description: String) {
    Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier.padding(16.dp),
        maxLines = 5,
        overflow = TextOverflow.Ellipsis
    )
}

@Preview
@Composable
fun DetailsScreenPreview() {
    Surface {
        DetailsScreen(
            seriesDetails = ExtendedSeries(
                episodes = listOf(),
                seasons = mapOf(),
                name = "Really off the rails asdfasdf asdfasdf asdfasdf",
                year = "1989",
                id = 1,
                imageUrl = "https://www.thetvdb.com/banners/posters/71663-1.jpg",
                description = "This is a description"
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}