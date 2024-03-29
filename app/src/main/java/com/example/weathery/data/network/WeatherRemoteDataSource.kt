package com.example.weathery.data.network

import com.example.weathery.data.models.WeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface WeatherRemoteDataSource {
    suspend fun getWeatherForecast(lon: Double,
                                   lat: Double,
                                   lang: String? = null,
                                   units: String? = null) : Flow<WeatherResponse>
}

object WeatherRemoteDataSourceImpl: WeatherRemoteDataSource {
    override suspend fun getWeatherForecast(lon: Double, lat: Double, lang: String?, units: String?) = flow<WeatherResponse> {
        emit(RetrofitHelper.retrofitWeatherService.getWeatherForecast(lon, lat, lang, units).body()!!)
    }.flowOn(Dispatchers.IO)
}

