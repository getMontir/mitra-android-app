package com.getmontir.lib.data.data.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.getmontir.lib.data.data.entity.CityEntity
import java.util.*

data class CityDto(
    @JsonProperty(value = "id")
    val id: String,

    @JsonProperty(value = "name")
    val name: String
) {
    fun entity(provinceId: String): CityEntity {
        return CityEntity(
            id = id,
            name = name,
            provinceId = provinceId,
            lastRefreshed = Date()
        )
    }
}
