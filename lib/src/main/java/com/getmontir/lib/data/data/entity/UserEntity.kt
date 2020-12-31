package com.getmontir.lib.data.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class UserEntity(
    @PrimaryKey var id: String,
    val image: String?,
    val name: String,
    val phone: String?,
    val email: String,
    val banned: Boolean,
    val accepted: Boolean,
    val informationCompleted: Boolean,
    val documentCompleted: Boolean,
    val emailVerified: String?,
    override var lastRefreshed: Date
): BaseEntity()
