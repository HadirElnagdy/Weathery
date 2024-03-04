package com.example.weathery.weatherscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weathery.models.WeatherRepository
import java.lang.IllegalArgumentException

class WeatherViewModelFactory(private val repo: WeatherRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WeatherViewModel::class.java)){
            WeatherViewModel(repo) as T
        }else{
            throw IllegalArgumentException("View Model is not found")
        }
    }
}