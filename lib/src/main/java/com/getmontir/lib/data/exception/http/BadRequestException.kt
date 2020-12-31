package com.getmontir.lib.data.exception.http

import com.getmontir.lib.data.exception.HttpBaseException

class BadRequestException(e: Throwable): HttpBaseException(e)