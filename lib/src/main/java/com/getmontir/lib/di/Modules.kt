package com.getmontir.lib.di

import com.getmontir.lib.data.data.AppDatabase
import com.getmontir.lib.data.network.APIService
import com.getmontir.lib.data.repository.eloquent.AuthRepository
import com.getmontir.lib.data.repository.eloquent.ProvinceRepository
import com.getmontir.lib.data.repository.eloquent.VersionRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val remoteModule = module {
    single { APIService.createService(androidContext(), get()) }
}

val repositoryModule = module {
    factory { VersionRepository( androidContext(), get() ) }
    factory { ProvinceRepository( androidContext(), get(), get() ) }
    factory { AuthRepository( androidContext(), get(), get() ) }
}

val databaseModules = module {
    single<AppDatabase> { AppDatabase.buildDatabase(androidContext()) }
    factory { get<AppDatabase>().provinceDao() }
    factory { get<AppDatabase>().cityDao() }
    factory { get<AppDatabase>().districtDao() }
}