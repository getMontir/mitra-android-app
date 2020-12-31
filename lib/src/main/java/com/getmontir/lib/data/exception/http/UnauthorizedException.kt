package com.getmontir.lib.data.exception.http

import com.getmontir.lib.data.exception.HttpBaseException

class UnauthorizedException(e: Throwable): HttpBaseException(e)