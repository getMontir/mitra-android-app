package com.getmontir.lib.data.response

import retrofit2.Response

sealed class ResultWrapper<out T: Any> {
    data class Success<out T: Any>(val data: Any?): ResultWrapper<T>()
    sealed class Error(open val exception: Exception, open val data: Any? = null): ResultWrapper<Nothing>() {
        class GenericError(exception: Exception): Error(exception)
        sealed class Http(override val exception: Exception, override val data: Any? = null): Error(exception) {
            class BadRequest(exception: Exception): Http(exception)
            class NotFound(exception: Exception): Http(exception)
            class Maintenance(exception: Exception): Http(exception)
            class Unauthorized(exception: Exception): Http(exception)
            class BadMethod(exception: Exception): Http(exception)
            class ServerError(exception: Exception): Http(exception)
            class Validation(exception: Exception, response: ApiErrorValidation?): Http(exception,response)
        }
        sealed class Network(override val exception: Exception): Error(exception) {
            class NoConnectivity(exception: Exception): Network(exception)
        }
    }
    data class Loading(val loading: Boolean): ResultWrapper<Nothing>()
    val <T> T.exhaustive: T
        get() = this
}
