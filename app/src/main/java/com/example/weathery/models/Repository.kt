package com.example.weathery.models

import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getWeatherForecast(lon: Double, lat: Double) : Flow<WeatherResponse>

}