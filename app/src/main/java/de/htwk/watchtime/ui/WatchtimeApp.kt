package de.htwk.watchtime.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.htwk.watchtime.R
import de.htwk.watchtime.ui.screens.DetailsScreen
import de.htwk.watchtime.ui.screens.HomeScreen
import de.htwk.watchtime.ui.screens.SearchScreen
import de.htwk.watchtime.ui.screens.StatsScreen
import de.htwk.watchtime.ui.screens.shared.components.WatchtimeAppBar

enum class WatchtimeScreens(@StringRes val title: Int) {
    Home(title = R.string.app_name),
    Search(title = R.string.search_screen),
    Details(title = R.string.details_screen),
    Stats(title = R.string.stats_screen),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchtimeApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = WatchtimeScreens.valueOf(
        backStackEntry?.destination?.route ?: WatchtimeScreens.Home.name
    )

    Scaffold(
        topBar = {
            WatchtimeAppBar(
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateBack = { navController.navigateUp() },
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = WatchtimeScreens.Home.name
        ) {
            composable(route = WatchtimeScreens.Home.name) {
                HomeScreen(modifier = Modifier.padding(innerPadding))
            }
            composable(route = WatchtimeScreens.Details.name) {
                DetailsScreen(modifier = Modifier.padding(innerPadding))
            }
            composable(route = WatchtimeScreens.Search.name) {
                SearchScreen(modifier = Modifier.padding(innerPadding))
            }
            composable(route = WatchtimeScreens.Stats.name) {
                StatsScreen(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

@Preview
@Composable
fun WatchtimeAppPreview() {
    WatchtimeApp()
}