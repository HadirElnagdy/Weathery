package com.example.weathery.main.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weathery.data.models.Settings
import com.example.weathery.data.repositories.SettingsRepository
import java.lang.IllegalArgumentException

class SettingsViewModel(private val repo: SettingsRepository) : ViewModel() {
    fun getLanguage() = repo.getLanguage()
    fun setLanguage(language: String) = repo.setLanguage(language)
    fun getUnits() = repo.getUnits()
    fun setUnits(units: String) = repo.setUnits(units)
    fun isNotificationEnabled() = repo.isNotificationEnabled()
    fun setNotificationEnabled(enabled: Boolean) = repo.setNotificationEnabled(enabled)
    fun getSettings(): Settings = repo.getSettings()
    fun saveSettings(settings: Settings) = repo.saveSettings(settings)
}

class SettingsViewModelFactory(private val repo: SettingsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SettingsViewModel::class.java)){
            SettingsViewModel(repo) as T
        }else{
            throw IllegalArgumentException("View Model is not found")
        }
    }
}