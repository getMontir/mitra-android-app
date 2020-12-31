package com.getmontir.mitra

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.getmontir.mitra.di.appModules
import com.google.firebase.database.FirebaseDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import timber.log.Timber

open class GetApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        // TIMBER
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // KOIN
        configureDi()
        loadKoinModules(appModules)

        // Firebase Setup
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)

        // Facebook
        FacebookSdk.sdkInitialize(applicationContext);
        AppEventsLogger.activateApp(this);
    }

    open fun configureDi() = startKoin {
        androidContext(this@GetApplication)
        appModules
    }

    // PUBLIC API
    open fun proviceComponent() = appModules

}