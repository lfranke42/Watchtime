package de.htwk.watchtime.ui.screens.shared.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import de.htwk.watchtime.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchtimeTopAppBar(
    modifier: Modifier = Modifier,
    canNavigateBack: Boolean,
    navigateBack: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = { AppBarTitle() },
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateBack) {
                    Icon(
                        modifier = Modifier.fillMaxSize(),
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = stringResource(id = R.string.navigate_back),
                    )
                }
            }
        }
    )
}

@Composable
private fun AppBarTitle() {
    Text(
        text = stringResource(id = R.string.app_name),
        style = MaterialTheme.typography.headlineLarge,
    )
}

@Preview
@Composable
fun WatchTimeAppBarPreview() {
    WatchtimeTopAppBar(
        canNavigateBack = false,
        navigateBack = {}
    )
}

@Preview
@Composable
fun WatchTimeAppBarWithBackPreview() {
    WatchtimeTopAppBar(
        canNavigateBack = true,
        navigateBack = {}
    )
}