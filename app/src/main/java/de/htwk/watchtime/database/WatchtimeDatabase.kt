package de.htwk.watchtime.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import de.htwk.watchtime.data.db.EpisodeDbEntry
import de.htwk.watchtime.data.db.SeriesDbEntry
import de.htwk.watchtime.data.db.UserWatchtimeDbEntry

@Database(
    entities = [SeriesDbEntry::class, EpisodeDbEntry::class, UserWatchtimeDbEntry::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class WatchtimeDatabase : RoomDatabase() {
    abstract fun watchtimeDao(): WatchtimeDao
}