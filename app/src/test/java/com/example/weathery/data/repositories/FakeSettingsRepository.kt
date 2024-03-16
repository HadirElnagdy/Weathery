package com.example.weathery.data.repositories

import com.example.weathery.data.models.Settings

class FakeSettingsRepository: SettingsRepository {

    private var language = "english"
    private var units = "metric"
    private var notification = false
    override fun getLanguage(): String {
        TODO("Not yet implemented")
    }

    override fun setLanguage(language: String) {
        TODO("Not yet implemented")
    }

    override fun getUnits(): String {
        TODO("Not yet implemented")
    }

    override fun setUnits(units: String) {
        TODO("Not yet implemented")
    }

    override fun isNotificationEnabled(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setNotificationEnabled(enabled: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getSettings(): Settings {
        TODO("Not yet implemented")
    }

    override fun saveSettings(settings: Settings) {
        TODO("Not yet implemented")
    }
}