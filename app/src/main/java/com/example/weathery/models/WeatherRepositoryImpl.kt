package com.example.weathery.models

import com.example.weathery.network.RetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

object WeatherRepositoryImpl: WeatherRepository {

    override suspend fun getWeatherForecast(lon: Double, lat: Double) = flow<WeatherResponse> {
        emit(RetrofitHelper.retrofitWeatherService.getWeatherForecast(lon, lat).body()!!)
    }.flowOn(Dispatchers.IO)

}