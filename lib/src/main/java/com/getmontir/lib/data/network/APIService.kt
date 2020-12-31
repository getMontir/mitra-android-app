package com.getmontir.lib.data.network

import android.content.Context
import com.getmontir.lib.data.response.ApiResponse
import com.getmontir.lib.BuildConfig
import com.getmontir.lib.data.data.dto.*
import com.getmontir.lib.ext.bodyToString
import com.getmontir.lib.data.network.interceptor.NetworkConnectionInterceptor
import com.getmontir.lib.presentation.utils.SessionManager
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

interface APIService {
    @FormUrlEncoded
    @POST("check-update/customer")
    suspend fun checkUpdateCustomerAsync(
        @Field("req_version") version: Int
    ): Response<ApiResponse<Boolean>>

    @FormUrlEncoded
    @POST("check-update/station")
    suspend fun checkUpdateStationAsync(
        @Field("req_version") version: Int
    ): Response<ApiResponse<Boolean>>

    @GET("provinces")
    suspend fun getProvincesAsync(): Response<ApiResponse<List<ProvinceDto>>>

    @GET("cities")
    suspend fun getCitiesAsync(): Response<ApiResponse<List<CityDto>>>

    @GET("districts")
    suspend fun getDistrictsAsync(): Response<ApiResponse<List<DistrictDto>>>

    @FormUrlEncoded
    @POST("province/cities")
    suspend fun getCitiesFromProvinceAsync(
        @Field("province_id") provinceId: String
    ): Response<ApiResponse<List<CityDto>>>

    @FormUrlEncoded
    @POST("city/districts")
    suspend fun getDistrictsFromCityAsync(
        @Field("city_id") cityId: String
    ): Response<ApiResponse<List<DistrictDto>>>

    @FormUrlEncoded
    @POST("customer/auth")
    suspend fun customerLoginAsync(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Response<ApiResponse<String>>

    @FormUrlEncoded
    @POST("customer/auth/social")
    suspend fun customerLoginSocialAsync(
        @Field("token") token: String,
        @Field("fcm_token") fcmToken: String,
        @Field("channel") channel: String,
        @Field("device") device: String
    ): Response<ApiResponse<String>>

    @FormUrlEncoded
    @POST("customer/register")
    suspend fun customerRegisterAsync(
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("password_confirmation") passwordConfirmation: String
    ): Response<ApiResponse<String>>

    @FormUrlEncoded
    @POST("customer/register/social")
    suspend fun customerRegisterSocialAsync(
        @Field("token") token: String,
        @Field("fcm_token") fcmToken: String,
        @Field("channel") channel: String,
        @Field("device") device: String
    ): Response<ApiResponse<String>>

    @FormUrlEncoded
    @POST("customer/password/forgot")
    suspend fun customerForgotPasswordAsync(
        @Field("email") email: String
    ): Response<ApiResponse<String>>

    @FormUrlEncoded
    @POST("customer/pasword/forgot/resend")
    suspend fun customerForgotPasswordResendAsync(
        @Field("email") email: String
    ): Response<ApiResponse<String>>

    @FormUrlEncoded
    @POST("customer/password/forgot/confirm")
    suspend fun customerForgotPasswordConfirmAsync(
        @Field("otp") otp: String,
        @Field("token") token: String,
        @Field("email") email: String
    ): Response<ApiResponse<String>>

    @FormUrlEncoded
    @POST("customer/password/change")
    suspend fun customerForgotChangePasswordAsync(
        @Field("token") token: String,
        @Field("password") password: String,
        @Field("password_confirmation") passwordConfirmation: String,
        @Field("email") email: String
    ): Response<ApiResponse<String>>

    @POST("profile")
    suspend fun profileUserAsync(): Response<ApiResponse<UserDto>>

    @GET("customer/user")
    suspend fun customerProfileAsync(): Response<ApiResponse<DetailCustomerDto>>

    companion object {

        private const val debugURL = "http://getmontir.paperplane.id/api/"
        private const val releaseURL = "https://api.getmontir.com/api/"

        fun createService( context: Context, sessionManager: SessionManager): APIService {
            val baseURL = if( BuildConfig.DEBUG ) debugURL else releaseURL
            val appToken = "bCtgjoy3gGQHAdoyzFbduGhAGr5hQND5Fbt7ggMWNgi10_dcPBmr9cHc5tK9v"
            val cacheTime = 432000

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.HEADERS
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(NetworkConnectionInterceptor(context))
                .addInterceptor(interceptor)
                .addInterceptor { chain ->
                    val newRequest = chain.request()
                    val request: Request

                    val builder = newRequest.newBuilder()

                    if( sessionManager.isLoggedIn ) {
                        builder.addHeader("Authorization", "Bearer " + sessionManager.token)
                    }

                    if( listOf("put", "patch", "delete").contains(
                            newRequest.method.toLowerCase(
                                Locale.ROOT
                            )
                        )
                    ) {
                        val body = newRequest.body
                        val method = newRequest.method
                        builder.post(
                            (body.bodyToString() + "&method=$method"
                                    ).toRequestBody(body?.contentType())
                        )
                    }

                    builder
                        .addHeader("X-Requested-With", "XMLHttpRequest")
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("Accept", "application/json")
                        .removeHeader("Pragma")
                        .addHeader("Cache-Control", String.format("max-age=%d", cacheTime))
                        .addHeader("X-G-Access-Token", appToken)

                    request = builder.build()

                    Timber.d("API URL:: ${request.url}")

                    chain.proceed(request)
                }
                .build()

            return Retrofit.Builder()
                .baseUrl(baseURL)
                .client(okHttpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build().create(APIService::class.java)
        }
    }
}