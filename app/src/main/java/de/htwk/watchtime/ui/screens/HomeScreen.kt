package de.htwk.watchtime.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import de.htwk.watchtime.R
import de.htwk.watchtime.data.Series
import de.htwk.watchtime.ui.screens.shared.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onCardTap: (seriesId: Int) -> Unit = {},
    viewModel: HomeViewModel = koinViewModel()
) {
    val seriesList by viewModel.series.collectAsState()
    HomeScreen(
        recommendedSeries = seriesList,
        continueWatchingList = seriesList,
        onCardTap = onCardTap,
        modifier = modifier
    )
}

@Composable
fun HomeScreen(
    recommendedSeries: List<Series>,
    continueWatchingList: List<Series>,
    modifier: Modifier = Modifier,
    onCardTap: (seriesId: Int) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(start = 24.dp, end = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            HomeScreenTitle(text = stringResource(id = R.string.home_screen_continue_watching_title))
        }
        item {
            ContinueWatchingCarousel(continueWatchingList = continueWatchingList, onCardTap = onCardTap)
        }
        item {
            HomeScreenTitle(text = stringResource(id = R.string.home_screen_popular_title))
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
        modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(continueWatchingList) { series ->
            CarouselCard(series, onCardTap = onCardTap)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarouselCard(
    series: Series,
    modifier: Modifier = Modifier,
    onCardTap: (seriesId: Int) -> Unit = {}
) {
    Card(
        onClick = { onCardTap(series.id) },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        modifier = modifier
            .height(256.dp)
            .width(164.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = series.name,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            AsyncImage(
                model = "https://artworks.thetvdb.com${series.imageUrl}",
                contentDescription = stringResource(id = R.string.home_screen_image_desc),
                contentScale = ContentScale.Crop
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeriesCard(
    series: Series,
    onTap: (seriesId: Int) -> Unit = {},
) {
    var contentScale by remember {
        mutableStateOf(ContentScale.Crop)
    }

    val currentIconColor = if (isSystemInDarkTheme()) Color.White else Color.Black
    var colorFilter by remember { mutableStateOf<ColorFilter?>(null) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        onClick = { onTap(series.id) }
    ) {
        Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(50.dp),
                color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
            ) {
                AsyncImage(
                    model = "https://artworks.thetvdb.com${series.imageUrl}",
                    contentDescription = stringResource(id = R.string.home_screen_image_desc),
                    fallback = painterResource(id = R.drawable.outline_movie_24),
                    error = painterResource(id = R.drawable.outline_movie_24),
                    onError = {
                        contentScale = ContentScale.Inside
                        colorFilter = ColorFilter.tint(currentIconColor)
                    },
                    onSuccess = {
                        contentScale = ContentScale.Crop
                        colorFilter = null

                    },
                    contentScale = contentScale,
                    colorFilter = colorFilter
                )
            }

            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = series.name,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(text = series.year, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Preview
@Composable
fun CarouselCardPreview() {
    CarouselCard(
        series = Series(
            name = "Breaking Bad",
            year = "2013",
            imageUrl = "/img/asdf399",
            id = 1
        )
    )
}

@Preview
@Composable
fun SeriesCardPreview() {
    SeriesCard(
        series = Series(
            name = "Breaking Bad",
            year = "2013",
            imageUrl = "/img/asdf399",
            id = 1
        )
    )
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