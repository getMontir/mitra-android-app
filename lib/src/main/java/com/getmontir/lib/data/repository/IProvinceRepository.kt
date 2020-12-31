package com.getmontir.lib.data.repository

import com.getmontir.lib.data.data.entity.ProvinceEntity
import com.getmontir.lib.data.response.ResultWrapper
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow

interface IProvinceRepository {
    @InternalCoroutinesApi
    fun all(): Flow<ResultWrapper<List<ProvinceEntity>>>
}