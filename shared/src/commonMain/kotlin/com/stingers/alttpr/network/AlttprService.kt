package com.stingers.alttpr.network

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Url

interface AlttprService {

    @GET("api/daily")
    suspend fun getDaily(): DailyResponse

    @GET("api/h/{hash}")
    suspend fun getSeed(@Path("hash") hash: String): SeedResponse

    @GET
    suspend fun getBpsPatch(@Url url: String): ByteArray
}
