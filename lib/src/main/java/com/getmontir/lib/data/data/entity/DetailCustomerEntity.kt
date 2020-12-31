package com.getmontir.lib.data.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.getmontir.lib.data.data.dto.CityDto
import com.getmontir.lib.data.data.dto.CustomerVehicleDto
import com.getmontir.lib.data.data.dto.DistrictDto
import com.getmontir.lib.data.data.dto.ProvinceDto
import java.util.*

@Entity
data class DetailCustomerEntity(
    @PrimaryKey var id: String,
    var memberId: String?,
    var birthPlace: String?,
    var birthDate: String?,
    var gender: String?,
    var address: String?,
    var province: ProvinceDto?,
    var city: CityDto?,
    var district: DistrictDto?,
    var postcode: String?,
    var vehicles: List<CustomerVehicleDto>,
    override var lastRefreshed: Date
): BaseEntity()