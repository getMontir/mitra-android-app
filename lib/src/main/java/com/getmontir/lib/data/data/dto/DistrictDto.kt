package com.getmontir.lib.data.data.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.getmontir.lib.data.data.entity.DistrictEntity
import java.util.*

data class DistrictDto(
    @JsonProperty(value = "id")
    val id: String,

    @JsonProperty(value = "name")
    val name: String
) {
   fun entity(cityId: String): DistrictEntity {
       return DistrictEntity(
           id = id,
           name = name,
           cityId = cityId,
           lastRefreshed = Date()
       )
   }
}
