package com.getmontir.lib.ext

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber
import java.lang.Exception

fun Any?.isNull(f: ()-> Unit){
    if (this != null){
        f()
    }
}

fun multiPartBobyPart(file: java.io.File?, parameterName: String): MultipartBody.Part {
    return MultipartBody.Part.createFormData(parameterName, if (file == null) "noFile" else file.name,
            (file
                    ?: java.io.File("")).asRequestBody("multipart/form-data".toMediaTypeOrNull()))
}

fun getRequestBody(parameter: String): RequestBody {
    return parameter.toRequestBody("multipart/form-data".toMediaTypeOrNull())
}


fun multiPartBobyPart(data: String, parameterName: String): MultipartBody.Part {
    return MultipartBody.Part.createFormData(parameterName, data)
}

fun isOnline(context: Context): Boolean {

    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ) {
        val capabilities = connectivityManager.getNetworkCapabilities( connectivityManager.activeNetwork )
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR ) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI ) -> {
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET ) -> {
                    return true
                }
            }
        }
    } else {

        try {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null) {
                if( activeNetworkInfo.isConnected ) {
                    return true
                }
            }
        } catch ( e: Exception ) {
            Timber.tag("ExtIsOnline").e(e.cause)
        }
    }

    return false
}