package com.getmontir.lib.data.data.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.getmontir.lib.data.data.entity.UserEntity
import java.util.*

data class UserDto(
    @JsonProperty(value = "id")
    val id: String,

    @JsonProperty(value = "image")
    val image: String?,

    @JsonProperty(value = "name")
    val name: String,

    @JsonProperty(value = "phone")
    val phone: String?,

    @JsonProperty(value = "email")
    val email: String,

    @JsonProperty(value = "banned")
    val banned: Boolean,

    @JsonProperty(value = "accepted")
    val accepted: Boolean,

    @JsonProperty(value = "information_completed")
    val informationCompleted: Boolean,

    @JsonProperty(value = "documentation_completed")
    val documentCompleted: Boolean,

    @JsonProperty(value = "email_verified")
    val emailVerified: String?

) {

    fun entity(): UserEntity {
        return UserEntity(
            id = id,
            image = image,
            name = name,
            phone = phone,
            email = email,
            banned = banned,
            accepted = accepted,
            informationCompleted = informationCompleted,
            documentCompleted = documentCompleted,
            emailVerified = emailVerified,
            lastRefreshed = Date()
        )
    }

}
