package com.demo.dhiwise.local

import android.content.Context
import android.content.SharedPreferences

object PreferencesManager {
    private lateinit var sharedPreferences: SharedPreferences


    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
    }

    fun saveToken(token: String) {
        sharedPreferences.edit().putString("AUTH_TOKEN", token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("AUTH_TOKEN", null)
    }
}
