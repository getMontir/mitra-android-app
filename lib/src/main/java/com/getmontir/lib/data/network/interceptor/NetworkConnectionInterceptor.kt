package com.getmontir.lib.data.network.interceptor

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.getmontir.lib.data.exception.network.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class NetworkConnectionInterceptor(private val context: Context): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if( !isOnline() ) {
            throw NoConnectivityException(Throwable("No Internet Connection"))
        }

        val builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

    fun isOnline(): Boolean {

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

}