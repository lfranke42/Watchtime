package de.htwk.watchtime.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.htwk.watchtime.R
import de.htwk.watchtime.ui.screens.HomeScreen
import de.htwk.watchtime.ui.screens.SearchScreen
import de.htwk.watchtime.ui.screens.StatsScreen
import de.htwk.watchtime.ui.screens.shared.components.WatchtimeBottomAppBar
import de.htwk.watchtime.ui.screens.shared.components.WatchtimeTopAppBar

enum class WatchtimeScreens(@StringRes val title: Int, @StringRes val navDescription: Int, @DrawableRes val icon: Int) {
    Home(title = R.string.home_screen, navDescription = R.string.navigate_home, icon = R.drawable.outline_home_24),
    Search(title = R.string.search_screen, navDescription = R.string.navigate_search, icon = R.drawable.outline_search_24),
    Details(title = R.string.details_screen, navDescription = R.string.navigate_details, icon = R.drawable.outline_more_horiz_24),
    Stats(title = R.string.stats_screen, navDescription = R.string.navigate_stats, icon = R.drawable.baseline_insert_chart_outlined_24),
}

@Composable
fun WatchtimeApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = WatchtimeScreens.valueOf(
        backStackEntry?.destination?.route ?: WatchtimeScreens.Home.name
    )

    Scaffold(
        topBar = {
            WatchtimeTopAppBar(
                canNavigateBack = false,
                navigateBack = { navController.navigateUp() },
            )
        },
        bottomBar = {
            WatchtimeBottomAppBar(
                currentScreen = currentScreen,
                navToHome = { navController.navigate(WatchtimeScreens.Home.name) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                } },
                navToSearch = { navController.navigate(WatchtimeScreens.Search.name) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }},
                navToStats = { navController.navigate(WatchtimeScreens.Stats.name) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }},
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