package com.example.weathery.data.repositories

import com.example.weathery.data.models.Settings

class FakeSettingsRepository: SettingsRepository {

    private var language = "english"
    private var units = "metric"
    private var notification = false
    override fun getLanguage() = language

    override fun setLanguage(language: String) {
        this.language = language
    }

    override fun getUnits() = units

    override fun setUnits(units: String) {
        this.units = units
    }

    override fun isNotificationEnabled(): Boolean = notification

    override fun setNotificationEnabled(enabled: Boolean) {
        this.notification = enabled
    }

    override fun getSettings(): Settings {
        TODO("Not yet implemented")
    }

    override fun saveSettings(settings: Settings) {
        TODO("Not yet implemented")
    }
}