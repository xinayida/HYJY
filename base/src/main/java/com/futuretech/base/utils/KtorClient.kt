package com.futuretech.base.utils

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

private const val TIME_OUT = 6000

open class BaseResult<T> : Result()

@Polymorphic
sealed class Result {
    object Loading : Result()
    data class Error(val exception: Exception) : Result()
    class Success<T>(val data: T) : BaseResult<T>()
}

@Serializable
data class ErrorResult(val code: Int, val message: String)

class CustomResponseException(response: HttpResponse, cachedResponseText: String, code: Int, message: String) :
    ResponseException(response, cachedResponseText) {
    override val message: String = "Custom server error: ${response.call.request.url}. " +
            "Status: ${response.status}. Text: \"$cachedResponseText\""
}
suspend inline fun <reified T : Any> get(url: String): Result {
    return try {
        Result.Success<T>(httpClientAndroid.get(url).body())
    } catch (e: Exception) {
        Result.Error(e)
    }
}
suspend inline fun <reified T : Any> post(url: String, data: T): Result {
    return try {
        Result.Success(httpClientAndroid.post(url) {
            setBody(data)
        })
    } catch (e: Exception) {
        Result.Error(e)
    }
}

val httpClientAndroid = HttpClient(Android) {

    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })

        engine {
            connectTimeout = TIME_OUT
            socketTimeout = TIME_OUT
        }
    }

    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Log.v("Logger Ktor =>", message)
            }

        }
        level = LogLevel.ALL
    }

    install(ResponseObserver) {
        onResponse { response ->
            Log.d("HTTP status:", "${response.status.value}")
        }
    }

    install(DefaultRequest) {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
    }
//    install(ResponseValidation) addDefaultResponseValidation()
    HttpResponseValidator {
        validateResponse { response ->
            val error: ErrorResult = response.body()
            if (error.code != 0) {
                throw CustomResponseException(
                    response, "Code: ${error.code}, message: ${error.message}",
                    code = error.code, message = error.message
                )
            }
        }
    }
}