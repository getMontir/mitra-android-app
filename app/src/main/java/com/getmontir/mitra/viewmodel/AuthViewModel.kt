package com.getmontir.mitra.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getmontir.lib.data.data.dto.StationRegisterContactDto
import com.getmontir.lib.data.data.entity.UserEntity
import com.getmontir.lib.data.repository.eloquent.AuthRepository
import com.getmontir.lib.data.response.ResultWrapper
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repo: AuthRepository
) : ViewModel() {
    private val _token = MutableLiveData<ResultWrapper<String>>()
    val token: LiveData<ResultWrapper<String>> get() = _token

    private val _forgotToken = MutableLiveData<ResultWrapper<String>>()
    val forgotToken: LiveData<ResultWrapper<String>> get() = _forgotToken

    private val _resendForgotToken = MutableLiveData<ResultWrapper<String>>()
    val resendForgotToken: LiveData<ResultWrapper<String>> get() = _resendForgotToken

    private val _user = MutableLiveData<ResultWrapper<UserEntity>>()
    val user: LiveData<ResultWrapper<UserEntity>> get() = _user

    private val _stationRegisterContact = MutableLiveData<ResultWrapper<StationRegisterContactDto>>()
    val stationRegisterContract: LiveData<ResultWrapper<StationRegisterContactDto>> get() = _stationRegisterContact

    @InternalCoroutinesApi
    fun stationLogin(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            repo.stationLogin(email,password)
                .collect {
                    _token.value = it
                }

        }
    }

    @InternalCoroutinesApi
    fun stationLoginGoogle(
        token: String,
        fcmToken: String
    ) {
       viewModelScope.launch {
           repo.stationLoginSocial(
               token, fcmToken, "google", "android"
           )
               .collect {
                   _token.value = it
               }
       }
    }

    @InternalCoroutinesApi
    fun stationLoginFacebook(
        token: String,
        fcmToken: String
    ) {
        viewModelScope.launch {
           repo.stationLoginSocial(
               token, fcmToken, "facebook", "android"
           )
               .collect {
                   _token.value = it
               }
       }
    }

    @InternalCoroutinesApi
    fun stationRegisterContact() {
        viewModelScope.launch {
            repo.stationRegisterContact()
                .collect {
                    _stationRegisterContact.value = it
                }
        }
    }

    @InternalCoroutinesApi
    fun stationForgotPassword(
        email: String
    ) {
        viewModelScope.launch {
            repo.stationForgotPassword(email)
                .collect {
                    _forgotToken.value = it
                }
        }
    }

    @InternalCoroutinesApi
    fun stationForgotConfirm(
        otp: String,
        token: String,
        email: String
    ) {
        viewModelScope.launch {
            repo.stationForgotPasswordConfirm(otp, token, email)
                .collect {
                    _forgotToken.value = it
                }
        }
    }

    @InternalCoroutinesApi
    fun stationResendForgotCode(
        email: String
    ) {
        viewModelScope.launch {
            repo.stationForgotPasswordResend(email)
                .collect {
                    _resendForgotToken.value = it
                }
        }
    }

    @InternalCoroutinesApi
    fun stationChangePassword(
        token: String,
        password: String,
        passwordConfirmation: String,
        email: String
    ) {
        viewModelScope.launch {
            repo.stationForgotPasswordReset( token, password, passwordConfirmation, email )
                .collect {
                    _forgotToken.value = it
                }
        }
    }

    @InternalCoroutinesApi
    fun mechanicLogin(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            repo.mechanicLogin(email, password)
                .collect {
                    _token.value = it
                }
        }
    }

    @InternalCoroutinesApi
    fun mechanicLoginGoogle(
        token: String,
        fcmToken: String
    ) {
        viewModelScope.launch {
            repo.mechanicLoginSocial(token, fcmToken, "google", "android")
                .collect {
                    _token.value = it
                }
        }
    }

    @InternalCoroutinesApi
    fun mechanicLoginFacebook(
        token: String,
        fcmToken: String
    ) {
        viewModelScope.launch {
            repo.mechanicLoginSocial(token, fcmToken, "facebook", "android")
                .collect {
                    _token.value = it
                }
        }
    }

    @InternalCoroutinesApi
    fun mechanicRegister(
        name: String,
        phone: String,
        email: String,
        password: String,
        passwordConfirmation: String
    ) {
       viewModelScope.launch {
           repo.customerRegister( name, phone, email, password, passwordConfirmation )
               .collect {
                   _token.value = it
               }
       }
    }

    @InternalCoroutinesApi
    fun mechanicRegisterGoogle(
        token: String,
        fcmToken: String
    ) {
        viewModelScope.launch {
            repo.mechanicRegisterSocial( token, fcmToken, "google", "android" )
                .collect {
                    _token.value = it
                }
        }
    }

    @InternalCoroutinesApi
    fun mechanicRegisterFacebook(
        token: String,
        fcmToken: String
    ) {
        viewModelScope.launch {
            repo.mechanicRegisterSocial( token, fcmToken, "facebook", "android" )
                .collect {
                    _token.value = it
                }
        }
    }

    @InternalCoroutinesApi
    fun mechanicForgotPassword(
        email: String
    ) {
        viewModelScope.launch {
            repo.mechanicForgotPassword(email)
                .collect {
                    _forgotToken.value = it
                }
        }
    }

    @InternalCoroutinesApi
    fun mechanicForgotConfirm(
        otp: String,
        token: String,
        email: String
    ) {
        viewModelScope.launch {
            repo.mechanicForgotPasswordConfirm(otp, token, email)
                .collect {
                    _forgotToken.value = it
                }
        }
    }

    @InternalCoroutinesApi
    fun mechanicResendForgotCode(
        email: String
    ) {
        viewModelScope.launch {
            repo.mechanicForgotPasswordResend(email)
                .collect {
                    _resendForgotToken.value = it
                }
        }
    }

    @InternalCoroutinesApi
    fun mechanicChangePassword(
        token: String,
        password: String,
        passwordConfirmation: String,
        email: String
    ) {
        viewModelScope.launch {
            repo.mechanicForgotPasswordReset( token, password, passwordConfirmation, email )
                .collect {
                    _forgotToken.value = it
                }
        }
    }

    @InternalCoroutinesApi
    fun profile() {
        viewModelScope.launch {
            repo.profile()
                .collect {
                    _user.value = it
                }
        }
    }
}