package com.getmontir.lib.data.exception.network

import java.io.IOException

class NoConnectivityException(throwable: Throwable): IOException(throwable){

    override val message: String?
        get() = "No Internet Connection"
}