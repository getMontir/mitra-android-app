package com.getmontir.mitra.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getmontir.lib.data.repository.eloquent.VersionRepository
import com.getmontir.lib.data.response.ResultWrapper
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SplashScreenViewModel(
    private val repo: VersionRepository
) : ViewModel() {
    private val _version = MutableLiveData<ResultWrapper<Boolean>>()
    val version: LiveData<ResultWrapper<Boolean>> get() = _version

    @InternalCoroutinesApi
    fun loadVersion(
        version: Int
    ) {
       viewModelScope.launch {
           repo.getVersionPartner(version)
               .collect {
                   _version.value = it
               }
       }
    }
}