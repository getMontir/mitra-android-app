package com.getmontir.lib.data.repository.eloquent

import android.content.Context
import com.getmontir.lib.data.network.APIService
import com.getmontir.lib.data.network.ApiResourceBound
import com.getmontir.lib.data.response.ApiResponse
import com.getmontir.lib.data.response.ResultWrapper
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class VersionRepository(
    private val context: Context,
    private val api: APIService
) {
    @InternalCoroutinesApi
    fun getVersionCustomer(version: Int): Flow<ResultWrapper<Boolean>> {
//        return flow {
//            val list = api.checkUpdateCustomerAsync(version)
//            emit(list)
//        }.flowOn(Dispatchers.IO)
        return object: ApiResourceBound<Boolean, ApiResponse<Boolean>>(context, false) {
            override fun processResponse(response: ApiResponse<Boolean>?): Boolean? {
                return response?.data
            }

            override fun shouldFetch(data: Boolean?): Boolean {
                return true
            }

            override suspend fun saveCallResults(items: Boolean) {
                // DO NOTHING
            }

            override suspend fun loadFromDb(): Boolean? {
                return null
            }

            override suspend fun createCallAsync(): Response<ApiResponse<Boolean>> {
                return api.checkUpdateCustomerAsync(version)
            }

        }.build()
    }
}