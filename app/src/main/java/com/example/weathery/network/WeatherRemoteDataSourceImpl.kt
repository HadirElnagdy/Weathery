package com.example.weathery.network

import com.example.weathery.models.WeatherResponse

object WeatherRemoteDataSourceImpl: WeatherRemoteDataSource {

    override suspend fun getWeatherForecast(lon: Double, lat: Double): WeatherResponse {
        return RetrofitHelper.retrofitWeatherService.getWeatherForecast(lon, lat).body()!!
    }

}