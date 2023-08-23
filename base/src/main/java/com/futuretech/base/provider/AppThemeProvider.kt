package com.futuretech.base.provider

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


enum class AppTheme {
    Light,
    Dark,
    Gray;
}

object AppThemeProvider {

    private const val KEY_GROUP = "AppThemeGroup"

    private const val KEY_APP_THEME = "keyAppTheme"

    private lateinit var preferences: SharedPreferences

    private val defaultTheme = AppTheme.Light

    var appTheme by mutableStateOf(value = defaultTheme)
        private set

    fun init(application: Application) {
        preferences = application.getSharedPreferences(KEY_GROUP, Context.MODE_PRIVATE)
        appTheme = getAppThemeOfDefault()
        initThemeDelegate(appTheme = appTheme)
    }

    private fun getAppThemeOfDefault(): AppTheme {
        val themeIndex = preferences.getInt(KEY_APP_THEME, defaultTheme.ordinal)
        return AppTheme.values().find { it.ordinal == themeIndex } ?: defaultTheme
    }

    fun onAppThemeChanged(appTheme: AppTheme) {
        preferences.edit().putInt(KEY_APP_THEME, appTheme.ordinal).apply()
        initThemeDelegate(appTheme = appTheme)
        this.appTheme = appTheme
    }

    private fun initThemeDelegate(appTheme: AppTheme) {
        when (appTheme) {
            AppTheme.Light, AppTheme.Gray -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            AppTheme.Dark -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

}