package com.getmontir.lib.data.exception.http

import com.getmontir.lib.data.exception.HttpBaseException

class ServerMaintenanceException(e: Throwable): HttpBaseException(e)