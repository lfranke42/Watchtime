package de.htwk.watchtime.network.ranking

import de.htwk.watchtime.BuildConfig
import de.htwk.watchtime.data.Ranking
import de.htwk.watchtime.network.NetworkRequestException
import de.htwk.watchtime.network.dto.RankingRequest
import de.htwk.watchtime.network.dto.toRanking
import java.util.*

interface RemoteRankingDataSource {
    suspend fun updateWatchtime(newTotalWatchtime: Long)
    suspend fun getRanking(): Ranking
    suspend fun deleteUser()
}

class RemoteRankingDataSourceImpl: RemoteRankingDataSource {
    override suspend fun updateWatchtime(newTotalWatchtime: Long) {
        val apiKey = BuildConfig.AZURE_FUNCTION_KEY
        val deviceId = UUID.randomUUID().toString()

        val rankingRequestBody = RankingRequest(deviceId, newTotalWatchtime)

        val updateWatchtimeResponse = rankingApi.updateWatchtime(apiKey, rankingRequestBody)

        if (!updateWatchtimeResponse.isSuccessful) {
            throw NetworkRequestException("Error updating watchtime")
        }
    }

    override suspend fun getRanking(): Ranking {
        val apiKey = BuildConfig.AZURE_FUNCTION_KEY
        val deviceId = UUID.randomUUID().toString()

        val rankingResponse = rankingApi.getRanking(apiKey, deviceId)
        val responseBody = rankingResponse.body()

        return if (rankingResponse.isSuccessful && responseBody != null)
            responseBody.toRanking()
        else {
            throw NetworkRequestException("Error fetching ranking")
        }
    }

    override suspend fun deleteUser() {
        val apiKey = BuildConfig.AZURE_FUNCTION_KEY
        val deviceId = UUID.randomUUID().toString()

        val deleteResponse = rankingApi.deleteUser(apiKey, deviceId)

        if (!deleteResponse.isSuccessful) {
            throw NetworkRequestException("Error deleting user")
        }
    }
}