package com.getmontir.lib.data.repository.eloquent

import android.content.Context
import com.getmontir.lib.data.data.dto.UserDto
import com.getmontir.lib.data.data.entity.UserEntity
import com.getmontir.lib.data.network.APIService
import com.getmontir.lib.data.network.ApiResourceBound
import com.getmontir.lib.data.response.ApiResponse
import com.getmontir.lib.data.response.ResultWrapper
import com.getmontir.lib.presentation.session
import com.getmontir.lib.presentation.utils.SessionManager
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import java.util.*

class AuthRepository(
    private val context: Context,
    private val api: APIService,
    private val sessionManager: SessionManager
) {

    @InternalCoroutinesApi
    fun customerLogin(
        email: String?,
        password: String?
    ): Flow<ResultWrapper<String>> = object: ApiResourceBound<String, ApiResponse<String>>(context) {
        override fun processResponse(response: ApiResponse<String>?): String? {
            return response?.data
        }

        override fun shouldFetch(data: String?): Boolean {
            return true
        }

        override suspend fun saveCallResults(items: String) {
            sessionManager.isLoggedIn = true
            sessionManager.token = items
        }

        override suspend fun loadFromDb(): String? {
            return sessionManager.token
        }

        override suspend fun createCallAsync(): Response<ApiResponse<String>> {
            return api.customerLoginAsync(email, password)
        }

    }.build()

    @InternalCoroutinesApi
    fun customerLoginSocial(
        token: String,
        fcmToken: String,
        channel: String,
        device: String
    ): Flow<ResultWrapper<String>> = object: ApiResourceBound<String, ApiResponse<String>>(context) {
        override fun processResponse(response: ApiResponse<String>?): String? {
            return response?.data
        }

        override fun shouldFetch(data: String?): Boolean {
            return true
        }

        override suspend fun saveCallResults(items: String) {
            sessionManager.isLoggedIn = true
            sessionManager.token = items
        }

        override suspend fun loadFromDb(): String? {
            return sessionManager.token
        }

        override suspend fun createCallAsync(): Response<ApiResponse<String>> {
            return api.customerLoginSocialAsync(token, fcmToken, channel, device)
        }

    }.build()

    @InternalCoroutinesApi
    fun customerRegister(
        name: String,
        phone: String,
        email: String,
        password: String,
        passwordConfirmation: String
    ): Flow<ResultWrapper<String>> = object: ApiResourceBound<String, ApiResponse<String>>(context) {

        override fun processResponse(response: ApiResponse<String>?): String? {
            return response?.data
        }

        override fun shouldFetch(data: String?): Boolean {
            return true
        }

        override suspend fun saveCallResults(items: String) {
            sessionManager.isLoggedIn = true
            sessionManager.token = items
        }

        override suspend fun loadFromDb(): String? {
            return sessionManager.token
        }

        override suspend fun createCallAsync(): Response<ApiResponse<String>> {
            return api.customerRegisterAsync(name, phone, email, password, passwordConfirmation)
        }

    }.build()

    @InternalCoroutinesApi
    fun customerRegisterSocial(
        token: String,
        fcmToken: String,
        channel: String,
        device: String
    ): Flow<ResultWrapper<String>> = object: ApiResourceBound<String, ApiResponse<String>>(context) {

        override fun processResponse(response: ApiResponse<String>?): String? {
            return response?.data
        }

        override fun shouldFetch(data: String?): Boolean {
            return true
        }

        override suspend fun saveCallResults(items: String) {
            sessionManager.isLoggedIn = true
            sessionManager.token = items
        }

        override suspend fun loadFromDb(): String? {
            return sessionManager.token
        }

        override suspend fun createCallAsync(): Response<ApiResponse<String>> {
            return api.customerRegisterSocialAsync(token, fcmToken, channel, device)
        }

    }.build()

    @InternalCoroutinesApi
    fun customerForgotPassword(
        email: String
    ): Flow<ResultWrapper<String>> = object: ApiResourceBound<String, ApiResponse<String>>(context) {
        override fun processResponse(response: ApiResponse<String>?): String? {
            return response?.data
        }

        override fun shouldFetch(data: String?): Boolean {
            return true
        }

        override suspend fun saveCallResults(items: String) {
            sessionManager.forgotEmail = email
            sessionManager.forgotToken = items
        }

        override suspend fun loadFromDb(): String? {
            return sessionManager.forgotToken
        }

        override suspend fun createCallAsync(): Response<ApiResponse<String>> {
            return api.customerForgotPasswordAsync(email)
        }

    }.build()

    @InternalCoroutinesApi
    fun customerForgotPasswordConfirm(
        otp: String,
        token: String,
        email: String
    ): Flow<ResultWrapper<String>> = object: ApiResourceBound<String, ApiResponse<String>>(context) {
        override fun processResponse(response: ApiResponse<String>?): String? {
            return response?.data
        }

        override fun shouldFetch(data: String?): Boolean {
            return true
        }

        override suspend fun saveCallResults(items: String) {
            sessionManager.forgotToken = items
        }

        override suspend fun loadFromDb(): String? {
            return sessionManager.forgotToken
        }

        override suspend fun createCallAsync(): Response<ApiResponse<String>> {
            return api.customerForgotPasswordConfirmAsync( otp, token, email )
        }
    }.build()

    @InternalCoroutinesApi
    fun customerForgotChangePassword(
        token: String,
        password: String,
        passwordConfirmation: String,
        email: String
    ): Flow<ResultWrapper<String>> = object: ApiResourceBound<String, ApiResponse<String>>(context) {
        override fun processResponse(response: ApiResponse<String>?): String? {
            return response?.data
        }

        override fun shouldFetch(data: String?): Boolean {
            return true
        }

        override suspend fun saveCallResults(items: String) {
            sessionManager.forgotEmail = null
            sessionManager.forgotToken = null
        }

        override suspend fun loadFromDb(): String? {
            return null
        }

        override suspend fun createCallAsync(): Response<ApiResponse<String>> {
            return api.customerForgotChangePasswordAsync(token, password, passwordConfirmation, email)
        }
    }.build()

    @InternalCoroutinesApi
    fun customerForgotPasswordResend(
        email: String
    ): Flow<ResultWrapper<String>> = object: ApiResourceBound<String, ApiResponse<String>>(context) {
        override fun processResponse(response: ApiResponse<String>?): String? {
            return response?.data
        }

        override fun shouldFetch(data: String?): Boolean {
            return true
        }

        override suspend fun saveCallResults(items: String) {
            sessionManager.forgotToken = items
        }

        override suspend fun loadFromDb(): String? {
            return sessionManager.forgotToken
        }

        override suspend fun createCallAsync(): Response<ApiResponse<String>> {
            return api.customerForgotPasswordResendAsync(email)
        }
    }.build()

    @InternalCoroutinesApi
    fun profile(): Flow<ResultWrapper<UserEntity>> = object: ApiResourceBound<UserEntity, ApiResponse<UserDto>>(context) {
        override fun processResponse(response: ApiResponse<UserDto>?): UserEntity? {
            return response?.data?.entity()
        }

        override fun shouldFetch(data: UserEntity?): Boolean {
            return true
        }

        override suspend fun saveCallResults(items: UserEntity) {
            sessionManager.userId = items.id
            sessionManager.userImage = items.image
            sessionManager.userName = items.name
            sessionManager.userEmail = items.email
            sessionManager.userBanned = items.banned
            sessionManager.userAccepted = items.accepted
            sessionManager.userInformationCompleted = items.informationCompleted
            sessionManager.userDocumentCompleted = items.documentCompleted
            sessionManager.userEmailVerifiedAt = items.emailVerified
        }

        override suspend fun loadFromDb(): UserEntity? {
            val userId = sessionManager.userId
            val userName = sessionManager.userName
            val userPhone = sessionManager.userPhone
            val userEmail = sessionManager.userEmail
            val userVerifiedAt = sessionManager.userEmailVerifiedAt
            if(
                userId != null
                && userName != null
                && userPhone != null
                && userEmail != null
                && userVerifiedAt != null
            ) {
                return UserEntity(
                    userId,
                    sessionManager.userImage,
                    userName,
                    userPhone,
                    userEmail,
                    sessionManager.userBanned,
                    sessionManager.userAccepted,
                    sessionManager.userInformationCompleted,
                    sessionManager.userDocumentCompleted,
                    userVerifiedAt,
                    lastRefreshed = Date()
                )
            } else {
                return null
            }
        }

        override suspend fun createCallAsync(): Response<ApiResponse<UserDto>> {
            return api.profileUserAsync()
        }

    }.build()

}