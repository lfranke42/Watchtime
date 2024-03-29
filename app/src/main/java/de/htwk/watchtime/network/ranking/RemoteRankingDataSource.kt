package de.htwk.watchtime.network.ranking

import android.content.res.Resources.NotFoundException
import de.htwk.watchtime.BuildConfig
import de.htwk.watchtime.data.Ranking
import de.htwk.watchtime.network.NetworkRequestException
import de.htwk.watchtime.network.dto.RankingRequest
import de.htwk.watchtime.network.dto.toRanking

interface RemoteRankingDataSource {
    suspend fun updateWatchtime(newTotalWatchtime: Long)
    suspend fun getRanking(): Ranking
    suspend fun deleteUser()
}

class RemoteRankingDataSourceImpl(private val deviceIdManager: DeviceIdManager) :
    RemoteRankingDataSource {
    override suspend fun updateWatchtime(newTotalWatchtime: Long) {
        val apiKey = BuildConfig.AZURE_FUNCTION_KEY
        val deviceId = deviceIdManager.getDeviceId()

        val rankingRequestBody = RankingRequest(deviceId, newTotalWatchtime)
        val updateWatchtimeResponse = rankingApi.updateWatchtime(
            functionKey = apiKey,
            rankingRequest = rankingRequestBody
        )

        if (!updateWatchtimeResponse.isSuccessful) {
            throw NetworkRequestException("Error updating watchtime")
        }
    }

    override suspend fun getRanking(): Ranking {
        val apiKey = BuildConfig.AZURE_FUNCTION_KEY
        val deviceId = deviceIdManager.getDeviceId()

        val rankingResponse = rankingApi.getRanking(id = deviceId, functionKey = apiKey)
        val responseBody = rankingResponse.body()

        return if (rankingResponse.isSuccessful && responseBody != null)
            responseBody.toRanking()
        else if (rankingResponse.code() == 404){
            throw NotFoundException("User has no entries")
        }
        else {
            throw NetworkRequestException("Error fetching ranking")
        }
    }

    override suspend fun deleteUser() {
        val apiKey = BuildConfig.AZURE_FUNCTION_KEY
        val deviceId = deviceIdManager.getDeviceId()

        val deleteResponse = rankingApi.deleteUser(id = deviceId, functionKey = apiKey)

        if (!deleteResponse.isSuccessful) {
            throw NetworkRequestException("Error deleting user")
        }
    }
}