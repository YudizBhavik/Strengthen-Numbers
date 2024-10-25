package com.demo.dhiwise

import android.app.Application
import com.demo.dhiwise.local.PreferencesManager

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        PreferencesManager.init(this)
    }
}
