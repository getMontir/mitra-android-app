package com.getmontir.lib.data.data.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.getmontir.lib.data.data.entity.DetailCustomerEntity
import java.util.*

data class DetailCustomerDto(
    @JsonProperty(value = "id")
    val id: String,

    @JsonProperty(value = "member_card")
    val memberId: String?,

    @JsonProperty(value = "birth_place")
    val birthPlace: String?,

    @JsonProperty(value = "birth_date")
    val birthDate: String?,

    @JsonProperty(value = "gender")
    val gender: String?,

    @JsonProperty(value = "address")
    val address: String?,

    @JsonProperty(value = "province")
    val province: ProvinceDto?,

    @JsonProperty(value = "city")
    val city: CityDto?,

    @JsonProperty(value = "district")
    val district: DistrictDto?,

    @JsonProperty(value = "postcode")
    val postcode: String?,

    @JsonProperty(value = "vehicles")
    val vehicles: List<CustomerVehicleDto>
) {

    fun entity(): DetailCustomerEntity {
        return DetailCustomerEntity(
            id = id,
            memberId = memberId,
            birthPlace = birthPlace,
            birthDate = birthDate,
            gender = gender,
            address = address,
            province = province,
            city = city,
            district = district,
            postcode = postcode,
            vehicles = vehicles,
            lastRefreshed = Date()
        )
    }

}