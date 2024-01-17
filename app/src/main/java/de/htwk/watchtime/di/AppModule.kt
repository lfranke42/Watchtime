package de.htwk.watchtime.di

import androidx.compose.ui.platform.LocalContext
import androidx.room.Room
import de.htwk.watchtime.database.LocalWatchtimeDataSource
import de.htwk.watchtime.database.LocalWatchtimeDataSourceImpl
import de.htwk.watchtime.database.WatchtimeDao
import de.htwk.watchtime.database.WatchtimeDatabase
import de.htwk.watchtime.database.WatchtimeRepository
import de.htwk.watchtime.database.WatchtimeRepositoryImpl
import de.htwk.watchtime.network.RemoteSeriesDataSource
import de.htwk.watchtime.network.RemoteSeriesDataSourceImpl
import de.htwk.watchtime.network.SeriesRepository
import de.htwk.watchtime.network.SeriesRepositoryImpl
import de.htwk.watchtime.network.SessionManager
import de.htwk.watchtime.ui.screens.shared.DetailsViewModel
import de.htwk.watchtime.ui.screens.shared.HomeViewModel
import de.htwk.watchtime.ui.screens.shared.SearchViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    singleOf(::RemoteSeriesDataSourceImpl) bind RemoteSeriesDataSource::class
    singleOf(::SeriesRepositoryImpl) bind SeriesRepository::class
    singleOf(::LocalWatchtimeDataSourceImpl) bind LocalWatchtimeDataSource::class
    singleOf(::WatchtimeRepositoryImpl) bind WatchtimeRepository::class
    singleOf(::SessionManager)
    singleOf(::LocalContext)
    single {
        Room.databaseBuilder(
            androidApplication(),
            WatchtimeDatabase::class.java,
            "watchtime-database"
        ).fallbackToDestructiveMigration().build()
    }
    single<WatchtimeDao> {
        val database = get<WatchtimeDatabase>()
        database.watchtimeDao()
    }
    viewModelOf(::HomeViewModel)
    viewModelOf(::DetailsViewModel)
    viewModelOf(::SearchViewModel)
}