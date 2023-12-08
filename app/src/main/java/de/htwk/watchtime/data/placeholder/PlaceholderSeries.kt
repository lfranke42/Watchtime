package de.htwk.watchtime.data.placeholder

import de.htwk.watchtime.data.Series

private val placeHolderSeries = Series(
    name = "",
    year = "",
    imageUrl = null,
    id = 0,
)

val placeHolderSeriesList = List(10) { placeHolderSeries }
