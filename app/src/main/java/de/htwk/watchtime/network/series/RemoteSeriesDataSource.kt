package de.htwk.watchtime.network.series

import de.htwk.watchtime.BuildConfig
import de.htwk.watchtime.network.NetworkRequestException
import de.htwk.watchtime.network.dto.LoginRequest
import de.htwk.watchtime.network.dto.SearchDto
import de.htwk.watchtime.network.dto.SeriesDto
import de.htwk.watchtime.network.dto.SeriesExtendedDto

interface RemoteSeriesDataSource {
    suspend fun getSeries(): List<SeriesDto>
    suspend fun getSeriesDetails(id: Int): SeriesExtendedDto?
    suspend fun searchSeries(name: String): List<SearchDto>
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
        var seriesDetailsResponse = tvdbApi.getSeriesDetails(authHeader = token, id = id)

        // Handle token expiration
        if (seriesDetailsResponse.code() == 401) {
            token = login()
            if (token == null) {
                throw Exception()
            }
            sessionManager.saveAuthToken(token)
            seriesDetailsResponse = tvdbApi.getSeriesDetails(authHeader = token, id = id)
        }

        val responseBody = seriesDetailsResponse.body()
        return if (seriesDetailsResponse.isSuccessful && responseBody != null)
            responseBody.data
        else {
            null
        }
    }

    override suspend fun searchSeries(name: String): List<SearchDto>{
        var token = sessionManager.fetchAuthToken()
        if (token == null) {
            token = login()
            if (token == null) {
                throw Exception()
            }
        }
        var searchResponse = tvdbApi.searchSeries(authHeader = token, name = name)

        // Handle token expiration
        if (searchResponse.code() == 401) {
            token = login()
            if (token == null) {
                throw Exception()
            }
            sessionManager.saveAuthToken(token)
            searchResponse = tvdbApi.searchSeries(authHeader = token, name = name)
        }

        val responseBody = searchResponse.body()
        return if(searchResponse.isSuccessful && responseBody != null)
         responseBody.data
        else{
           listOf()
        }
    }


    override suspend fun getSeries(): List<SeriesDto> {
        var token = sessionManager.fetchAuthToken()
        if (token == null) {
            token = login()
            if (token == null) {
                throw Exception("Could not obtain valid token")
            }
        }
        var seriesResponse = tvdbApi.getSeries(token)

        // Handle token expiration
        if (seriesResponse.code() == 401) {
            token = login()
            if (token == null) {
                throw Exception("Could not obtain valid token")
            }
            sessionManager.saveAuthToken(token)
            seriesResponse = tvdbApi.getSeries(token)
        }

        val responseBody = seriesResponse.body()
        return if (seriesResponse.isSuccessful && responseBody != null)
            responseBody.data
        else {
            throw NetworkRequestException("Failed to retrieve Series information")
        }
    }

    private suspend fun login(): String? {
        val apiKey = BuildConfig.API_KEY
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