package com.stingers.alttpr.di

import com.stingers.alttpr.repository.remote.AlttprApiException
import com.stingers.alttpr.repository.remote.AlttprService
import com.stingers.alttpr.repository.remote.createAlttprService
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
class NetworkModule {

    @Singleton
    fun provideHttpClient(
        log: com.stingers.alttpr.common.Logger
    ): HttpClient {
        return HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        isLenient = true
                        coerceInputValues = true
                    }, contentType = ContentType.Any
                )

            }
            install(DefaultRequest) {
                headers.append(HttpHeaders.Accept, "application/json")
            }
            HttpResponseValidator {
                validateResponse { response ->
                    if (!response.status.isSuccess()) {
                        throw AlttprApiException(response.status.value, response.bodyAsText())
                    }
                }
            }
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        // TODO Remove or hide behind debugMode
                        log.d("ALTTPR", message)
                    }
                }
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
