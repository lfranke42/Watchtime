package de.htwk.watchtime.network.ranking

import de.htwk.watchtime.data.Ranking

interface RankingRepository {
    suspend fun updateWatchtime(newTotalWatchtime: Long)
    suspend fun getRanking(): Ranking
    suspend fun deleteUser()
}


class RankingRepositoryImpl(private val dataSource: RemoteRankingDataSource) : RankingRepository {
    override suspend fun updateWatchtime(newTotalWatchtime: Long) {
        dataSource.updateWatchtime(newTotalWatchtime)
    }

    override suspend fun getRanking(): Ranking {
        return dataSource.getRanking()
    }

    override suspend fun deleteUser() {
        dataSource.deleteUser()
    }
}