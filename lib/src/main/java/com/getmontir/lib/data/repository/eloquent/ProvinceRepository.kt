package com.getmontir.lib.data.repository.eloquent

import android.content.Context
import com.getmontir.lib.data.data.dao.ProvinceDao
import com.getmontir.lib.data.data.dto.ProvinceDto
import com.getmontir.lib.data.data.entity.ProvinceEntity
import com.getmontir.lib.data.network.APIService
import com.getmontir.lib.data.network.ApiResourceBound
import com.getmontir.lib.data.network.apiResource
import com.getmontir.lib.data.repository.IProvinceRepository
import com.getmontir.lib.data.response.ApiResponse
import com.getmontir.lib.data.response.ResultWrapper
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.collect
import retrofit2.Response

class ProvinceRepository(
    private val context: Context,
    private val api: APIService,
    private val dao: ProvinceDao
): IProvinceRepository {

    @InternalCoroutinesApi
    override fun all(): Flow<ResultWrapper<List<ProvinceEntity>>> = object: ApiResourceBound<List<ProvinceEntity>, ApiResponse<List<ProvinceDto>>>(context) {
        override fun processResponse(response: ApiResponse<List<ProvinceDto>>?): List<ProvinceEntity> {
            val list: MutableList<ProvinceEntity> = arrayListOf()

            response?.data?.forEach {
                val model = it.entity()
                list.add(model)
            }

            return list
        }

        override fun shouldFetch(data: List<ProvinceEntity>?): Boolean {
            return true
        }

        override suspend fun saveCallResults(items: List<ProvinceEntity>) {
            dao.deletes()
            items.forEach {
                dao.inserts(it)
            }
        }

        override suspend fun loadFromDb(): List<ProvinceEntity>? {
            return dao.getAll()
        }

        override suspend fun createCallAsync(): Response<ApiResponse<List<ProvinceDto>>> {
            return api.getProvincesAsync()
        }

    }.build()

    val provinces: Flow<ResultWrapper<List<ProvinceEntity>>> by apiResource(context,{
        val list: MutableList<ProvinceEntity> = arrayListOf()

            it?.data?.forEach { item ->
                val model = item.entity()
                list.add(model)
            }

            return@apiResource list
    }, true, { items ->
        dao.deletes()
        items.forEach {
            dao.inserts(it)
        }
    }, {
        return@apiResource dao.getAll()
    }, {
        return@apiResource api.getProvincesAsync()
    })

}