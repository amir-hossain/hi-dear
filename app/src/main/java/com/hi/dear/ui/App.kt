package com.hi.dear.ui

import android.app.Application
import com.google.firebase.FirebaseApp
import com.hi.dear.BuildConfig
import timber.log.Timber
import timber.log.Timber.DebugTree


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        FirebaseApp.initializeApp(this)
    }
}