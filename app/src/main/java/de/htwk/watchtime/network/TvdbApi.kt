package de.htwk.watchtime.network

import de.htwk.watchtime.network.dto.LoginRequest
import de.htwk.watchtime.network.dto.LoginResponse
import de.htwk.watchtime.network.dto.SeriesEpisodesResponse
import de.htwk.watchtime.network.dto.SeriesResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface TvdbApi {
    @GET("series")
    suspend fun getSeries(@Header("Authorization") authHeader: String): Response<SeriesResponse>

    @POST("login")
    suspend fun login(
        @Header("Content-Type") contentType: String = "application/json",
        @Body loginRequest: LoginRequest,
    ): Response<LoginResponse>

    suspend fun getEpisodes(@Header("Authorization") authHeader: String): Response<SeriesEpisodesResponse>

}

private val retrofit = Retrofit.Builder()
    .baseUrl("https://api4.thetvdb.com/v4/")
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

val tvdbApi = retrofit.create(TvdbApi::class.java)