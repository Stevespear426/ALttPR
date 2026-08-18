package com.stingers.alttpr.repository.remote

import com.stingers.alttpr.common.SPRITES_URL
import com.stingers.alttpr.model.BasePatchInfoResponse
import com.stingers.alttpr.model.DailyResponse
import com.stingers.alttpr.model.SeedDetailsResponse
import com.stingers.alttpr.model.Sprite
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Url

interface AlttprService {

    @GET("api/daily")
    suspend fun getDaily(): DailyResponse

    @GET("api/h/{hash}")
    suspend fun getBasePatchInfo(@Path("hash") hash: String): BasePatchInfoResponse

    @GET("hash/{hash}")
    @Headers("Accept: application/json")
    suspend fun getSeedPatch(@Path("hash") hash: String): SeedDetailsResponse

    @GET
    suspend fun getBpsPatch(@Url url: String): ByteArray

    @GET
    suspend fun getSprites(@Url url: String = SPRITES_URL): List<Sprite>

    @GET
    suspend fun getSpriteFile(@Url url: String): ByteArray
}