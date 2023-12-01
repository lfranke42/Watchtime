package de.htwk.watchtime.di

import androidx.compose.ui.platform.LocalContext
import de.htwk.watchtime.network.RemoteSeriesDataSource
import de.htwk.watchtime.network.RemoteSeriesDataSourceImpl
import de.htwk.watchtime.network.SeriesRepository
import de.htwk.watchtime.network.SeriesRepositoryImpl
import de.htwk.watchtime.network.SessionManager
import de.htwk.watchtime.ui.screens.shared.DetailsViewModel
import de.htwk.watchtime.ui.screens.shared.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    singleOf(::RemoteSeriesDataSourceImpl) bind RemoteSeriesDataSource::class
    singleOf(::SeriesRepositoryImpl) bind SeriesRepository::class
    singleOf(::SessionManager)
    singleOf(::LocalContext)
    viewModelOf(::HomeViewModel)
    viewModelOf(::DetailsViewModel)
}