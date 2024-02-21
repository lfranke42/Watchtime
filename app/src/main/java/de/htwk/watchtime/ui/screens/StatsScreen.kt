package de.htwk.watchtime.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.htwk.watchtime.R
import de.htwk.watchtime.ui.screens.shared.StatsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun StatsScreen(modifier: Modifier = Modifier, viewModel: StatsViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    StatsScreenContent(
        modifier = modifier,
        totalWatchtime = uiState.totalWatchtime
    )
}

@Composable
private fun StatsScreenContent(
    modifier: Modifier = Modifier,
    totalWatchtime: String
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TotalWatchtimeCard(totalWatchtime = totalWatchtime)
        LeaderboardCard()
    }
}

@Composable
private fun TotalWatchtimeCard(totalWatchtime: String) {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.stats_screen_total_time_watched),
                style = MaterialTheme.typography.titleLarge
            )
            Text(text = totalWatchtime, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun LeaderboardCard() {
    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.stats_screen_leaderboard_title),
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}

@Preview
@Composable
fun StatsScreenPreview() {
    Surface {
        StatsScreen()
    }
}