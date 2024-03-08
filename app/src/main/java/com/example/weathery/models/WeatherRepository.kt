package com.example.weathery.models

import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeatherForecast(lon: Double, lat: Double) : Flow<WeatherResponse>

}