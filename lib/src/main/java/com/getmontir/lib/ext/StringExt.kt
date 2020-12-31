package com.getmontir.lib.ext

import android.annotation.SuppressLint
import android.text.format.DateUtils
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun String.convertToHuman(format: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"): CharSequence? {
    if( this.isEmpty() ) {
        return "-"
    }

    val format = SimpleDateFormat(format)
    val date = format.parse(this)

    return DateUtils.getRelativeTimeSpanString( date.time, Calendar.getInstance().timeInMillis, DateUtils.MINUTE_IN_MILLIS )
}

fun String.convertToCurrency(): String {
    val formatter: NumberFormat = DecimalFormat("#,###")
    val myNumber = this.toDouble()
    val formattedNumber = formatter.format(myNumber)

    return "Rp $formattedNumber"
}

