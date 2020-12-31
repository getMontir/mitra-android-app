package com.getmontir.lib.ext

import java.text.SimpleDateFormat
import java.util.*

fun Date.displayDate(): String? {
    if( this.toString().isEmpty() ) {
        return null
    }

    val outputFormatter = SimpleDateFormat("HH:mm")
    return outputFormatter.format(this)
}

fun Date.displayDateTime(): String? {
    if (this.toString().isEmpty()){
        return null
    }

    val outputFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm")

    return outputFormatter.format(this)
}

fun Date.displayDefaultDate(): String? {
    if( this.toString().isEmpty() ) {
        return null
    }

    val outputFormatter = SimpleDateFormat("yyyy-MM-dd")

    return outputFormatter.format(this)
}

fun String.displayDateTime(): String? {
    if( this.isEmpty() ) {
        return null
    }

    val outputFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm")

    return outputFormatter.format(this)
}