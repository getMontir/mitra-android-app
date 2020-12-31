package com.getmontir.lib.data.exception.http

import com.getmontir.lib.data.exception.HttpBaseException

class NotFoundException(e: Throwable): HttpBaseException(e)