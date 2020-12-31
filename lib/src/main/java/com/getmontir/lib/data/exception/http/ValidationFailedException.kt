package com.getmontir.lib.data.exception.http

import com.getmontir.lib.data.exception.HttpBaseException

class ValidationFailedException(e: Throwable): HttpBaseException(e)