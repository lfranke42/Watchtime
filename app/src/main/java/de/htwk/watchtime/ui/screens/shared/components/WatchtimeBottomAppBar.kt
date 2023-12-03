package de.htwk.watchtime.ui.screens.shared.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.htwk.watchtime.ui.WatchtimeScreens

val navigatableItems = listOf(
    WatchtimeScreens.Home,
    WatchtimeScreens.Search,
    WatchtimeScreens.Stats,
)

@Composable
fun WatchtimeBottomAppBar(
    currentScreen: String,
    navToHome: () -> (Unit) = { },
    navToSearch: () -> (Unit) = { },
    navToStats: () -> (Unit) = { }
) {
    NavigationBar(
        modifier = Modifier.fillMaxWidth(),
    ) {
        navigatableItems.forEach { screen ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(id = screen.icon),
                        contentDescription = stringResource(screen.navDescription)
                    )
                },
                label = { Text(text = stringResource(id = screen.title)) },
                onClick = {
                    when (screen) {
                        WatchtimeScreens.Home -> navToHome()
                        WatchtimeScreens.Search -> navToSearch()
                        WatchtimeScreens.Stats -> navToStats()
                        WatchtimeScreens.Details -> {}
                    }
                },
                selected = currentScreen == screen.name,
            )
        }
    }
}

@Preview
@Composable
fun WatchtimeBottomAppBarPreview() {
    WatchtimeBottomAppBar(currentScreen = WatchtimeScreens.Home.name)
}