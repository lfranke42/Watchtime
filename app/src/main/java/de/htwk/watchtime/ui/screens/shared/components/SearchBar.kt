package de.htwk.watchtime.ui.screens.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.htwk.watchtime.data.ExtendedSeries
import de.htwk.watchtime.data.Series
import de.htwk.watchtime.network.SeriesRepository
import de.htwk.watchtime.ui.screens.shared.SearchViewModel


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RoundedSearchBar(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel,
    onCardTap: (seriesId: Int) -> Unit = {}
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isSearching by viewModel.isSearching.collectAsState()
    val searchResult by viewModel.searchResult.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val density = LocalDensity.current.density
    val elevation = 4.dp * density

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(36.dp)
            )
            .padding(top = 16.dp)
            .padding(bottom = 16.dp)
            .shadow(elevation = elevation, shape = RoundedCornerShape(35.dp))
    ) {
        TextField(
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            value = searchQuery,
            onValueChange = {
                viewModel.setSearchQuery(it)
            },
            placeholder = {
                Text("Search for a series...")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    viewModel.performSearch()
                    keyboardController?.hide()
                }
            ),
            leadingIcon = {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            },
            trailingIcon = {
                if (isSearching) {
                    IconButton(
                        onClick = {
                            viewModel.clearSearch()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )

        if (isSearching) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                searchResult?.forEach { series ->
                    SeriesCard(series = series, onTap = onCardTap)

                } ?: run {
                    // Serie nicht gefunden
                    Text("Keine Serie gefunden für: $searchQuery")
                }
            }
        }
    }
}

@Preview
@Composable
fun RoundedSearchBarPreview() {

    val dummySeriesRepository = object : SeriesRepository {
        override suspend fun getSeries(): List<Series> {
            return listOf(
                Series(
                    name = "Breaking Bad",
                    id = 1,
                    year = "2008", // Beispieljahr
                    imageUrl = "https://example.com/breaking_bad_image.jpg"
                )
            )
        }

        override suspend fun getSeriesDetails(id: Int): ExtendedSeries {
            return ExtendedSeries(
                    name = "",
                    id = 1,
                    year = "",
                    imageUrl = null,
                    episodes = emptyList(),
                    seasons = emptyMap(),
                    description = null,
                    genres = emptyList()
                )

        }

        override suspend fun searchSeries(name: String): List<Series> {
            return listOf(
                Series(
                name = "Breaking Bad",
                id = 1,
                year = "2008",
                imageUrl = "https://example.com/breaking_bad_image.jpg"
            ))

        }

    }

    // SearchViewModel mit der Dummy SeriesRepository-Instanz erstellen
    val viewModel = remember { SearchViewModel(dummySeriesRepository) }

    // Deine RoundedSearchBar-Komponente für die Vorschau erstellen
    RoundedSearchBar(
        modifier = Modifier.fillMaxWidth(),
        viewModel = viewModel,
        onCardTap = {}
    )
}


