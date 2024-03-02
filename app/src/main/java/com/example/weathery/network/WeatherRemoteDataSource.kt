package com.example.weathery.network

import com.example.weathery.models.WeatherResponse

interface WeatherRemoteDataSource {
    suspend fun getWeatherForecast(long: Double, lat: Double) : WeatherResponse
}