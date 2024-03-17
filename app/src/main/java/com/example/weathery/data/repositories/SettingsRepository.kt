package com.example.weathery.data.repositories

import com.example.weathery.data.database.FavLocationLocalDataSource
import com.example.weathery.data.models.Settings
import com.example.weathery.data.network.WeatherRemoteDataSource
import com.example.weathery.data.sharedpreferences.SettingsLocalDataSource

interface SettingsRepository {
    fun getLanguage(): String
    fun setLanguage(language: String)
    fun getUnits(): String
    fun setUnits(units: String)
    fun isNotificationEnabled(): Boolean
    fun setNotificationEnabled(enabled: Boolean)
    fun getSettings(): Settings
    fun saveSettings(settings: Settings)
}

class SettingsRepositoryImpl private constructor(private val localDataSource: SettingsLocalDataSource): SettingsRepository{

    companion object{
        private var instance: SettingsRepositoryImpl? = null
        fun getInstance (localDataSource: SettingsLocalDataSource
        ): SettingsRepositoryImpl {
            return instance ?: synchronized(this){
                val temp = SettingsRepositoryImpl(localDataSource)
                instance = temp
                temp
            }
        }
    }
    override fun getLanguage() = localDataSource.getLanguage()
    override fun setLanguage(language: String) = localDataSource.setLanguage(language)
    override fun getUnits() = localDataSource.getUnits()
    override fun setUnits(units: String) = localDataSource.setUnits(units)
    override fun isNotificationEnabled() = localDataSource.isNotificationEnabled()
    override fun setNotificationEnabled(enabled: Boolean) = localDataSource.setNotificationEnabled(enabled)
    override fun getSettings(): Settings = localDataSource.getSettings()
    override fun saveSettings(settings: Settings) = localDataSource.saveSettings(settings)

}