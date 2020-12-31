package com.getmontir.lib.data.data.entity

import java.util.*
import java.util.concurrent.TimeUnit

abstract class BaseEntity {
    abstract var lastRefreshed: Date

    fun haveToRefreshFromNetwork(): Boolean = TimeUnit.MILLISECONDS.toMinutes(Date().time - lastRefreshed.time) >= 60
}