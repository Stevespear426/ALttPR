package com.stingers.alttpr.di

import com.stingers.alttpr.repository.remote.AlttprService
import com.stingers.alttpr.repository.remote.createAlttprService
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
class NetworkModule {

    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }

    @Singleton
    fun provideKtorfit(httpClient: HttpClient): Ktorfit {
        return Ktorfit.Builder()
            .httpClient(httpClient)
            .baseUrl("https://alttpr.com/")
            .build()
    }

    @Singleton
    fun provideAlttprService(ktorfit: Ktorfit): AlttprService {
        return ktorfit.createAlttprService()
    }
}
