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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.htwk.watchtime.data.Series


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RoundedSearchBar(
    modifier: Modifier = Modifier,
    seriesList: List<Series>,
    onCardTap: (seriesId: Int) -> Unit = {}

) {
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }
    var searchResult: Series? by remember { mutableStateOf(null) }
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
            //.offset(y = 25.dp)
            .shadow(elevation = elevation, shape = RoundedCornerShape(35.dp))

    ) {
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                // Suche in der Serienliste nach dem eingegebenen Text
                searchResult = seriesList.find { serie -> serie.name.equals(it, ignoreCase = true) }
            },
            placeholder = {
                Text("Suche nach einer Serie...")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    // Setze isSearching auf true und verstecke die Tastatur
                    isSearching = true
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
                            // Leere die Suchanfrage und setze den Suchergebnis-Status zurück
                            searchQuery = ""
                            isSearching = false
                            searchResult = null
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


    }
        if (isSearching) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    //.padding(16.dp)
            ) {
                searchResult?.let { series ->
                    // Serie gefunden, zeige die Serie an
                SeriesCard(series = series, onTap = onCardTap)


                } ?: run {
                    // Serie nicht gefunden
                    Text("Keine Serie gefunden für: $searchQuery")
                }
            }
        }

}


@Preview
@Composable
fun RoundedSearchBarPreview() {
        RoundedSearchBar(modifier = Modifier.fillMaxWidth(),
            seriesList = List(10) {
            Series(
                name = "Breaking Bad",
                year = "2013",
                imageUrl = "/img/asdf399",
                id = 1
            )
         })

}




