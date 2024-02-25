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
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.column.columnChart
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import de.htwk.watchtime.R
import de.htwk.watchtime.ui.screens.shared.StatsViewModel
import org.koin.androidx.compose.koinViewModel
import kotlin.math.roundToInt

@Composable
fun StatsScreen(modifier: Modifier = Modifier, viewModel: StatsViewModel = koinViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    StatsScreenContent(
        modifier = modifier,
        totalWatchtime = uiState.totalWatchtime,
        chartEntryModelProducer = uiState.chartEntryModelProducer,
        personalRank = uiState.personalRank,
        noTimeTracked = uiState.noTimeTracked
    )
}

@Composable
private fun StatsScreenContent(
    modifier: Modifier = Modifier,
    totalWatchtime: String,
    chartEntryModelProducer: ChartEntryModelProducer?,
    personalRank: Int?,
    noTimeTracked: Boolean,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TotalWatchtimeCard(totalWatchtime = totalWatchtime)
        LeaderboardCard(
            chartEntryModelProducer = chartEntryModelProducer,
            personalRank = personalRank,
            noTimeTracked = noTimeTracked,
        )
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
private fun LeaderboardCard(
    chartEntryModelProducer: ChartEntryModelProducer? = null,
    personalRank: Int? = null,
    noTimeTracked: Boolean
) {
    val xAxisValueFormatter =
        AxisValueFormatter<AxisPosition.Vertical.Start> { value, _ ->
            value.roundToInt().toString()
        }

    ElevatedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(R.string.stats_screen_leaderboard_title),
                style = MaterialTheme.typography.titleLarge
            )
            if (chartEntryModelProducer != null && personalRank != null) {
                Text(text = stringResource(R.string.stats_screen_personal_rank) + personalRank)

                Chart(
                    chart = columnChart(),
                    chartModelProducer = chartEntryModelProducer,
                    startAxis = rememberStartAxis(
                        title = stringResource(R.string.stats_screen_chart_hours_label),
                        titleComponent = textComponent(
                            textSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        valueFormatter = xAxisValueFormatter,
                        itemPlacer = AxisItemPlacer.Vertical.default(maxItemCount = 5)
                    ),
                    bottomAxis = rememberBottomAxis(
                        title = stringResource(R.string.stats_screen_chart_rank_label),
                        titleComponent = textComponent(
                            textSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (noTimeTracked) {
                        Text(text = stringResource(R.string.stats_screen_no_leaderboard_fallback))
                    } else {
                        CircularProgressIndicator(modifier = Modifier.size(48.dp))
                    }
                }
            }
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