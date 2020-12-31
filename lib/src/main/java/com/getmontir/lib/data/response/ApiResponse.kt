package com.getmontir.lib.data.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ApiResponse<T>(
    @JsonProperty( value = "data" )
    val data: T? = null,

    @JsonProperty( value = "response" )
    val response: String? = null,

    @JsonProperty(value="status_code")
    val statusCode: Int? = null,

    @JsonProperty(value="message")
    val message: String? = null,

    @JsonProperty(value = "exception")
    val exception: Exception? = null
)