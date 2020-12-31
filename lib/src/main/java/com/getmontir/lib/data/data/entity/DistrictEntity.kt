package com.getmontir.lib.data.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity( indices = [Index(value = ["city_id"])] )
data class DistrictEntity(
    @PrimaryKey var id: String,
    @ColumnInfo( name = "city_id" ) var cityId: String,
    var name: String,
    override var lastRefreshed: Date,
): BaseEntity()
