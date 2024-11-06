package com.sn.bhavik

import android.app.Application
import com.sn.bhavik.local.PreferencesManager

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PreferencesManager.init(this)
    }
}
