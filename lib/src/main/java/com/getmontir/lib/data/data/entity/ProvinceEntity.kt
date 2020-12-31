package com.getmontir.lib.data.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class ProvinceEntity(
    @PrimaryKey var id: String,
    var name: String,
    override var lastRefreshed: Date
): BaseEntity()
