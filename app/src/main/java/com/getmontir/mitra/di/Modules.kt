package com.getmontir.mitra.di

import com.getmontir.mitra.services.FirebaseAuthModule
import com.getmontir.mitra.services.FirebaseDatabaseModule
import com.getmontir.mitra.services.GoogleClientModule
import com.getmontir.lib.data.network.APIService
import com.getmontir.lib.di.databaseModules
import com.getmontir.lib.di.repositoryModule
import com.getmontir.lib.presentation.utils.SessionManager
import com.getmontir.mitra.viewmodel.AuthViewModel
import com.getmontir.mitra.viewmodel.SplashScreenViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val commonModule = module {
    single { SessionManager(androidContext(), "getMontirPartnerPref") }
    factory { FirebaseAuthModule.create() }
    factory { GoogleClientModule.create(androidContext()) }
    factory { FirebaseDatabaseModule.create() }
}

val remoteModule = module {
    single { APIService.createService(androidContext(), get(), "XTENKEUwN0OPHNvuuTkSlP2OILKkgmgdr4ausNRCKAJdR_D05CeQFoWoMoht3") }
}

val viewModelModule = module {
    viewModel { SplashScreenViewModel( get() ) }
    viewModel { AuthViewModel( get() ) }
}

val appModules = listOf(
    commonModule,
    remoteModule,
    databaseModules,
    repositoryModule,
    viewModelModule
)