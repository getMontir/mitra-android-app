package com.getmontir.lib.data.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ApiErrorValidation(
    @JsonProperty( value = "message" )
    val message: String,

    @JsonProperty( value = "errors" )
    val errors: ApiErrorValidationItem
)
