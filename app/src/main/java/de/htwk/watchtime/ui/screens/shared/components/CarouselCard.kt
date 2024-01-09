package de.htwk.watchtime.ui.screens.shared.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import de.htwk.watchtime.R
import de.htwk.watchtime.data.Series

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
