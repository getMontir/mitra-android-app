package com.getmontir.lib.data.data.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.getmontir.lib.data.data.entity.CustomerVehicleEntity
import java.util.*

data class CustomerVehicleDto(
    @JsonProperty(value = "id")
    val id: String,

    @JsonProperty(value = "image")
    val image: String?,

    @JsonProperty(value = "vehicle_brand")
    val vehicleBrand: String,

    @JsonProperty(value = "vehicle_type")
    val vehicleType: String,

    @JsonProperty(value = "vehicle_engine")
    val vehicleEngine: String,

    @JsonProperty(value = "vehicle_transmission")
    val vehicleTransmission: String,

    @JsonProperty(value = "vehicle_year")
    val vehicleYear: Int,

    @JsonProperty(value = "police_number")
    val policeNumber: String
) {

    fun entity(): CustomerVehicleEntity {
        return CustomerVehicleEntity(
            id = id,
            image = image,
            vehicleBrand = vehicleBrand,
            vehicleType = vehicleType,
            vehicleEngine = vehicleEngine,
            vehicleTransmission = vehicleTransmission,
            vehicleYear = vehicleYear,
            policeNumber = policeNumber,
            lastRefreshed = Date()
        )
    }

}