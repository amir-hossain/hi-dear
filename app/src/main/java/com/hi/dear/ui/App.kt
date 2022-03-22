package com.hi.dear.ui

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.FirebaseApp
import com.hi.dear.BuildConfig
import timber.log.Timber
import timber.log.Timber.DebugTree


class App : Application() {
    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
        FirebaseApp.initializeApp(this)
        instance = this
    }
}