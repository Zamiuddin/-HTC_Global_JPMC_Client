package com.app.weatherphoton

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseActivity : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}