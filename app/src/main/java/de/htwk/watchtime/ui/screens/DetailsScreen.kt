package de.htwk.watchtime.ui.screens

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import de.htwk.watchtime.ui.screens.shared.DetailsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: DetailsViewModel = koinViewModel()
) {

}

@Preview
@Composable
fun DetailsScreenPreview() {
    Surface {
        DetailsScreen()
    }
}