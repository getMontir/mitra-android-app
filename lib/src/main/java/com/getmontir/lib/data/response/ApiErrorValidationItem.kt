package com.getmontir.lib.data.response

import com.fasterxml.jackson.annotation.JsonProperty

data class ApiErrorValidationItem(

    @JsonProperty( value = "name" )
    val name: List<String>?,

    @JsonProperty( value = "phone" )
    val phone: List<String>?,

    @JsonProperty( value = "email" )
    val email: List<String>?,

    @JsonProperty( value = "password" )
    val password: List<String>?,

    @JsonProperty( value = "token" )
    val token: List<String>?,

    @JsonProperty( value = "fcm_token" )
    val fcmToken: List<String>?,

    @JsonProperty( value = "channel" )
    val channel: List<String>?,

    @JsonProperty( value = "device" )
    val device: List<String>?,

    @JsonProperty( value = "otp" )
    val otp: List<String>?

)
