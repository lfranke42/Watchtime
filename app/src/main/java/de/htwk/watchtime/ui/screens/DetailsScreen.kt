package de.htwk.watchtime.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import de.htwk.watchtime.R
import de.htwk.watchtime.data.ExtendedSeries
import de.htwk.watchtime.data.Season
import de.htwk.watchtime.ui.screens.shared.DetailsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel = koinViewModel()
) {
    val detailsScreenUiState by viewModel.seriesDetails.collectAsState()
    DetailsScreen(
        seriesDetails = detailsScreenUiState.seriesDetails,
        selectedSeason = detailsScreenUiState.selectedSeason,
        bottomSheetVisible = detailsScreenUiState.bottomSheetVisible,
        openBottomSheet = { viewModel.openBottomSheet() },
        closeBottomSheet = { viewModel.closeBottomSheet() },
        changeSeason = { viewModel.selectSeason(it) },
        modifier = modifier
    )
}

@Composable
fun DetailsScreen(
    seriesDetails: ExtendedSeries,
    selectedSeason: Int,
    bottomSheetVisible: Boolean,
    modifier: Modifier = Modifier,
    openBottomSheet: () -> Unit = {},
    closeBottomSheet: () -> Unit = {},
    changeSeason: (Int) -> Unit = {}
) {
    Box(modifier = modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
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
                item {
                    SeasonSelectionChip(
                        selectedSeason = selectedSeason,
                        openSeasonSelect = openBottomSheet
                    )
                }
            }
        }
    }

    if (bottomSheetVisible) {
        SeasonSelectBottomSheet(
            closeBottomSheet = closeBottomSheet,
            changeSeason = { changeSeason(it) },
            seasons = seriesDetails.seasons,
            selectedSeason = selectedSeason
        )
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
    surfaceHeight = if (imageUrl == "unavailable") 0.dp else 350.dp

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

@Composable
fun SeasonSelectionChip(
    modifier: Modifier = Modifier, selectedSeason: Int,
    openSeasonSelect: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AssistChip(onClick = openSeasonSelect, label = {
            Text(text = stringResource(id = R.string.details_screen_season_chip) + " " + selectedSeason)
        }, trailingIcon = {
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = stringResource(id = R.string.details_screen_select_season)
            )
        })
        Text(
            text = stringResource(id = R.string.details_screen_watched_title),
            style = MaterialTheme.typography.titleMedium,
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeasonSelectBottomSheet(
    selectedSeason: Int = 1,
    closeBottomSheet: () -> Unit = {},
    changeSeason: (Int) -> Unit = {},
    seasons: Map<Int, Season>
) {
    ModalBottomSheet(onDismissRequest = closeBottomSheet) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            seasons.forEach { (seasonNumber, _) ->
                item {
                    FilterChip(
                        onClick = { changeSeason(seasonNumber) },
                        selected = seasonNumber == selectedSeason,
                        label = {
                            Row(
                                modifier = Modifier.width(192.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                if (seasonNumber == selectedSeason) {
                                    Icon(
                                        Icons.Default.Check,
                                        contentDescription = stringResource(id = R.string.details_screen_select_season),
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                }
                                Text(
                                    text = stringResource(id = R.string.details_screen_season_chip) + " " + seasonNumber,
                                    textAlign = TextAlign.Center
                                )
                            }
                        },
                    )
                }
            }

        }
    }
}

@Preview
@Composable
fun DetailsScreenPreview() {
    Surface {
        DetailsScreen(
            seriesDetails = ExtendedSeries(
                episodes = listOf(),
                seasons = mapOf(),
                name = "Really off the rails long name for a series",
                year = "1989",
                id = 1,
                imageUrl = "https://www.thetvdb.com/banners/posters/71663-1.jpg",
                description = "This is a description"
            ),
            selectedSeason = 1,
            bottomSheetVisible = false,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
fun BottomSheetPreview() {
    SeasonSelectBottomSheet(seasons = mapOf(1 to Season(1, 1, mutableListOf(1))))
}