package de.htwk.watchtime.database

import androidx.room.Database
import androidx.room.RoomDatabase
import de.htwk.watchtime.data.db.SeriesDbEntry

@Database(entities = [SeriesDbEntry::class], version = 1)
abstract class WatchtimeDatabase : RoomDatabase() {
    abstract fun seriesDao(): WatchtimeDao
}