package de.htwk.watchtime.network

import de.htwk.watchtime.network.dto.LoginRequest
import de.htwk.watchtime.network.dto.SeriesDto

interface RemoteSeriesDataSource {
    suspend fun getSeries(): List<SeriesDto>
}

class RemoteSeriesDataSourceImpl(
    private val sessionManager: SessionManager
) : RemoteSeriesDataSource {

    override suspend fun getSeries(): List<SeriesDto> {
        var token = sessionManager.fetchAuthToken()
        if (token == null) {
            token = login()
            if (token == null) {
                throw Exception()
            }
        }

        val seriesResponse = tvdbApi.getSeries(token)
        val responseBody = seriesResponse.body()

        return if (seriesResponse.isSuccessful && responseBody != null)
            responseBody.data
        else {
            listOf()
        }
    }

    private suspend fun login(): String? {
        val loginResponse =
            tvdbApi.login(loginRequest = LoginRequest(apiKey = "d0361aab-14b3-4627-a5c9-17803769ce8a"))
        val responseBody = loginResponse.body()

        return if (loginResponse.isSuccessful && responseBody?.data != null && responseBody.data.token != null) {
            val token = responseBody.data.token
            sessionManager.saveAuthToken(token)
            token
        } else {
            null
        }
    }

}