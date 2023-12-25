package de.htwk.watchtime.network

import android.util.Log
import de.htwk.watchtime.BuildConfig
import de.htwk.watchtime.network.dto.LoginRequest
import de.htwk.watchtime.network.dto.SeriesDto
import de.htwk.watchtime.network.dto.SeriesExtendedDto

interface RemoteSeriesDataSource {
    suspend fun getSeries(): List<SeriesDto>
    suspend fun getSeriesDetails(id: Int): SeriesExtendedDto?
}

class RemoteSeriesDataSourceImpl(
    private val sessionManager: SessionManager
) : RemoteSeriesDataSource {

    override suspend fun getSeriesDetails(id: Int): SeriesExtendedDto? {
        var token = sessionManager.fetchAuthToken()
        if (token == null) {
            token = login()
            if (token == null) {
                throw Exception()
            }
        }

        val seriesDetailsResponse = tvdbApi.getSeriesDetails(authHeader = token, id = id)
        val responseBody = seriesDetailsResponse.body()

        return if (seriesDetailsResponse.isSuccessful && responseBody != null)
            responseBody.data
        else {
            null
        }
    }

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
        val apiKey = BuildConfig.API_KEY
        Log.w("API_KEY", apiKey)
        val loginResponse =
            tvdbApi.login(loginRequest = LoginRequest(apiKey = apiKey))
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