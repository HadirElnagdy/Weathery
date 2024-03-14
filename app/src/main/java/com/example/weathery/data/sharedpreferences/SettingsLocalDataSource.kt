package com.example.weathery.data.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.example.weathery.data.models.Settings
import com.example.weathery.utils.Constants
import com.example.weathery.utils.Constants.Companion
import com.example.weathery.utils.Constants.Companion.KEY_LANGUAGE
import com.example.weathery.utils.Constants.Companion.KEY_NOTIFICATION_ENABLED
import com.example.weathery.utils.Constants.Companion.KEY_UNITS

interface SettingsLocalDataSource {
    fun getLanguage(): String
    fun setLanguage(language: String)
    fun getUnits(): String
    fun setUnits(units: String)
    fun isNotificationEnabled(): Boolean
    fun setNotificationEnabled(enabled: Boolean)
    fun getSettings(): Settings
    fun saveSettings(settings: Settings)
}


class SettingsLocalDataSourceImpl(context: Context) : SettingsLocalDataSource {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        "settings_pref",
        Context.MODE_PRIVATE
    )

    private val DEFAULT_LANGUAGE = "en"
    private val DEFAULT_UNITS = "metric"
    private val DEFAULT_NOTIFICATION_ENABLED = false

    override fun getLanguage(): String {
        return sharedPreferences.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
    }

    override fun setLanguage(language: String) {
        sharedPreferences.edit().putString(KEY_LANGUAGE, language).apply()
    }

    override fun getUnits(): String {
        return sharedPreferences.getString(KEY_UNITS, DEFAULT_UNITS) ?: DEFAULT_UNITS
    }

    override fun setUnits(units: String) {
        sharedPreferences.edit().putString(KEY_UNITS, units).apply()
    }

    override fun isNotificationEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_NOTIFICATION_ENABLED, DEFAULT_NOTIFICATION_ENABLED)
    }

    override fun setNotificationEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_NOTIFICATION_ENABLED, enabled).apply()
    }

    override fun getSettings(): Settings {
        return Settings(
            language = sharedPreferences.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE,
            notificationEnabled = sharedPreferences.getBoolean(KEY_NOTIFICATION_ENABLED, DEFAULT_NOTIFICATION_ENABLED),
            units = sharedPreferences.getString(KEY_UNITS, DEFAULT_UNITS) ?: DEFAULT_UNITS,
        )
    }

    override fun saveSettings(settings: Settings) {
        sharedPreferences.edit().apply {
            putString(KEY_LANGUAGE, settings.language)
            putBoolean(KEY_NOTIFICATION_ENABLED, settings.notificationEnabled)
            putString(KEY_UNITS, settings.units)
            apply()
        }
    }
}


