package com.getmontir.lib.data.data.entity

import androidx.room.PrimaryKey
import java.util.*

data class CustomerVehicleEntity(
    @PrimaryKey var id: String,
    var image: String?,
    var vehicleBrand: String,
    var vehicleType: String,
    var vehicleEngine: String,
    var vehicleTransmission: String,
    var vehicleYear: Int,
    var policeNumber: String,
    override var lastRefreshed: Date
): BaseEntity()
