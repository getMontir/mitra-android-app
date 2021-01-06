package com.getmontir.mitra.utils.data

import androidx.annotation.DrawableRes
import com.getmontir.mitra.utils.enums.Role

data class AuthChooserItem(
    @DrawableRes val icon: Int,
    val role: Role,
    val name: String,
    val description: String?,
)