package de.htwk.watchtime.ui.screens

import android.widget.Toast
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import de.htwk.watchtime.R
import de.htwk.watchtime.data.Episode
import de.htwk.watchtime.data.ExtendedSeries
import de.htwk.watchtime.data.Season
import de.htwk.watchtime.ui.screens.shared.DetailsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel = koinViewModel()
) {
    val detailsScreenUiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current
    viewModel.message.observe(lifecycle) {
        it.getContentIfNotHandled()?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    DetailsScreen(
        seriesDetails = detailsScreenUiState.seriesDetails,
        selectedSeason = detailsScreenUiState.selectedSeason,
        bottomSheetVisible = detailsScreenUiState.bottomSheetVisible,
        seasonCompleted = detailsScreenUiState.seasonCompleted,
        seriesCompleted = detailsScreenUiState.seriesCompleted,
        dropDownExpanded = detailsScreenUiState.dropDownExpanded,
        toggleDropDown = { viewModel.toggleDropDown() },
        dismissDropDown = { viewModel.dismissDropDown() },
        toggleSeriesWatched = { viewModel.toggleSeriesWatched() },
        openBottomSheet = { viewModel.openBottomSheet() },
        closeBottomSheet = { viewModel.closeBottomSheet() },
        onSeasonWatchedChange = { season, completed ->
            viewModel.toggleSeasonWatched(
                season,
                completed
            )
        },
        changeSeason = { viewModel.selectSeason(it) },
        toggleEpisodeWatched = { episodeId, watched ->
            viewModel.toggleEpisodeWatched(
                episodeId,
                watched
            )
        },
        episodesWatched = detailsScreenUiState.episodesWatched,
        modifier = modifier
    )
}

@Composable
fun DetailsScreen(
    seriesDetails: ExtendedSeries,
    selectedSeason: Int,
    bottomSheetVisible: Boolean,
    seasonCompleted: Boolean,
    seriesCompleted: Boolean,
    dropDownExpanded: Boolean,
    modifier: Modifier = Modifier,
    toggleDropDown: () -> Unit = {},
    dismissDropDown: () -> Unit = {},
    toggleSeriesWatched: () -> Unit = {},
    openBottomSheet: () -> Unit = {},
    closeBottomSheet: () -> Unit = {},
    changeSeason: (Int) -> Unit = {},
    onSeasonWatchedChange: (Season, Boolean) -> Unit = { _, _ -> },
    toggleEpisodeWatched: (Int, Boolean) -> Unit = { _, _ -> },
    episodesWatched: Set<Int> = emptySet(),
) {
    val seasonEpisodes: List<Episode> =
        seriesDetails.episodes.filter { it.seasonNumber == selectedSeason }
    val scrollState = rememberScrollState()

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.verticalScroll(enabled = true, state = scrollState)) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                CardHeader(
                    title = seriesDetails.name,
                    year = seriesDetails.year,
                    seasons = seriesDetails.seasons.size.toString(),
                    episodes = seriesDetails.episodes.size.toString(),
                    seriesCompleted = seriesCompleted,
                    toggleSeriesWatched = toggleSeriesWatched,
                    dropDownExpanded = dropDownExpanded,
                    toggleDropDown = toggleDropDown,
                    dismissDropDown = dismissDropDown
                )
                SeriesImage(
                    imageUrl = seriesDetails.imageUrl ?: "unavailable",
                    modifier = Modifier
                        .fillMaxWidth()
                )
                if (seriesDetails.description != null) {
                    SeriesDescription(description = seriesDetails.description)
                }
                SeasonHeader(
                    selectedSeason = selectedSeason,
                    openSeasonSelect = openBottomSheet,
                    seasonCompleted = seasonCompleted,
                    onSeasonWatchedChange = {
                        val currentSeason = seriesDetails.seasons[selectedSeason] ?: Season(
                            id = 0,
                            seasonNumber = 0,
                            episodeIds = mutableListOf()
                        )
                        onSeasonWatchedChange(currentSeason, seasonCompleted)
                    }
                )

                seasonEpisodes.mapIndexed { index, episode ->
                    val checked = episodesWatched.contains(episode.id)
                    EpisodeRow(
                        episodeTitle = episode.name ?: "unknown name",
                        checked = checked,
                        episodeNumber = index + 1,
                        onCheckChange = { watched ->
                            toggleEpisodeWatched(
                                episode.id,
                                watched
                            )
                        }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
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
fun CardHeader(
    title: String,
    year: String,
    seasons: String,
    episodes: String,
    seriesCompleted: Boolean,
    dropDownExpanded: Boolean,
    toggleDropDown: () -> Unit = {},
    toggleSeriesWatched: () -> Unit = {},
    dismissDropDown: () -> Unit = {}
) {
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
        Box {
            IconButton(onClick = toggleDropDown) {
                Icon(
                    Icons.Default.MoreVert,
                    contentDescription = stringResource(id = R.string.details_screen_more_options)
                )
            }
            DropdownMenu(expanded = dropDownExpanded, onDismissRequest = dismissDropDown) {
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(stringResource(id = R.string.details_screen_series_completed))
                            Checkbox(
                                checked = seriesCompleted,
                                onCheckedChange = null,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    },
                    onClick = {
                        toggleSeriesWatched()
                        dismissDropDown()
                    })
            }
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
fun SeasonHeader(
    modifier: Modifier = Modifier,
    selectedSeason: Int,
    seasonCompleted: Boolean,
    openSeasonSelect: () -> Unit = {},
    onSeasonWatchedChange: (Boolean) -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
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
        WholeSeasonWatchedTickBox(
            seasonCompleted = seasonCompleted,
            onSeasonWatchedChange = onSeasonWatchedChange
        )
    }
}

@Composable
fun WholeSeasonWatchedTickBox(
    seasonCompleted: Boolean = false,
    onSeasonWatchedChange: (Boolean) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(id = R.string.details_screen_watched_title),
            style = MaterialTheme.typography.labelMedium,
        )
        Checkbox(checked = seasonCompleted, onCheckedChange = onSeasonWatchedChange)
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
                    SeasonChip(changeSeason, seasonNumber, selectedSeason)
                }
            }

        }
    }
}

@Composable
private fun SeasonChip(
    changeSeason: (Int) -> Unit,
    seasonNumber: Int,
    selectedSeason: Int
) {
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

@Composable
fun EpisodeRow(
    episodeTitle: String,
    modifier: Modifier = Modifier,
    episodeNumber: Int = 1,
    checked: Boolean,
    onCheckChange: (Boolean) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Text(
            text = "$episodeNumber ∙ $episodeTitle",
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Checkbox(checked = checked, onCheckedChange = { onCheckChange(it) })
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
                description = "This is a description",
                genres = listOf()
            ),
            selectedSeason = 1,
            bottomSheetVisible = false,
            seasonCompleted = false,
            seriesCompleted = false,
            dropDownExpanded = false,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview
@Composable
fun BottomSheetPreview() {
    SeasonSelectBottomSheet(seasons = mapOf(1 to Season(1, 1, mutableListOf(1))))
}