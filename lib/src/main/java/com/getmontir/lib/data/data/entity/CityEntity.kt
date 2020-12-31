package com.getmontir.lib.data.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(indices = [Index(value = ["province_id"])])
data class CityEntity(
    @PrimaryKey var id: String,
    @ColumnInfo(name = "province_id") var provinceId: String,
    var name: String,
    override var lastRefreshed: Date,
): BaseEntity()
