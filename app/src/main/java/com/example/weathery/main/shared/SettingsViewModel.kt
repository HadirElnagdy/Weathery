package com.example.weathery.main.shared

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weathery.data.repositories.SettingsRepository
import com.example.weathery.data.repositories.WeatherRepository
import java.lang.IllegalArgumentException

class SettingsViewModel(private val repo: SettingsRepository) : ViewModel() {

    

}

class SettingsViewModelFactory(private val repo: SettingsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WeatherViewModel::class.java)){
            SettingsViewModel(repo) as T
        }else{
            throw IllegalArgumentException("View Model is not found")
        }
    }
}