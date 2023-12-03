package de.htwk.watchtime.network

import de.htwk.watchtime.network.dto.LoginRequest
import de.htwk.watchtime.network.dto.LoginResponse
import de.htwk.watchtime.network.dto.SeriesExtendedResponse
import de.htwk.watchtime.network.dto.SeriesResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TvdbApi {
    @GET("series")
    suspend fun getSeries(@Header("Authorization") authHeader: String): Response<SeriesResponse>

    @POST("login")
    suspend fun login(
        @Header("Content-Type") contentType: String = "application/json",
        @Body loginRequest: LoginRequest,
    ): Response<LoginResponse>

    @GET("series/{id}/extended")
    suspend fun getSeriesDetails(
        @Header("Authorization") authHeader: String,
        @Path("id") id: Int,
        @Query("meta") meta: String = "episodes",
        @Query("short") short: Boolean = true,
    ): Response<SeriesExtendedResponse>

}

private val retrofit = Retrofit.Builder()
    .baseUrl("https://api4.thetvdb.com/v4/")
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

val tvdbApi = retrofit.create(TvdbApi::class.java)