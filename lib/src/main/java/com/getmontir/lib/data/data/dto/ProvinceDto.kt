package com.getmontir.lib.data.data.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.getmontir.lib.data.data.entity.ProvinceEntity
import java.util.*

data class ProvinceDto(
    @JsonProperty(value = "id")
    val id: String,

    @JsonProperty(value = "name")
    val name: String
) {
    fun entity(): ProvinceEntity {
        return ProvinceEntity(
            id = id,
            name = name,
            lastRefreshed = Date()
        )
    }
}