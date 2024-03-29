package de.htwk.watchtime.network.ranking

import de.htwk.watchtime.network.dto.RankingRequest
import de.htwk.watchtime.network.dto.RankingResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface RankingApi {
    @GET("ranking/position/{id}")
    suspend fun getRanking(
        @Path("id") id: String,
        @Query("code") functionKey: String
    ): Response<RankingResponse>


    @PUT("ranking/user")
    suspend fun updateWatchtime(
        @Query("code") functionKey: String,
        @Body rankingRequest: RankingRequest
    ): Response<Void>

    @DELETE("ranking/user/{id}")
    suspend fun deleteUser(
        @Path("id") id: String,
        @Query("code") functionKey: String
    ): Response<Void>

}

private val retrofit = Retrofit.Builder()
    .baseUrl("https://func-moco-lufr.azurewebsites.net/api/")
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

val rankingApi: RankingApi = retrofit.create(RankingApi::class.java)