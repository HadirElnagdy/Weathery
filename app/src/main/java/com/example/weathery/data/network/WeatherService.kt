package com.example.weathery.data.network

import com.example.weathery.data.models.WeatherResponse
import com.example.weathery.BuildConfig
import com.example.weathery.data.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("onecall")
    suspend fun getWeatherForecast(
        @Query("lon") lon: Double,
        @Query("lat") lat: Double,
        @Query("lang") lang: String? = null,
        @Query("units") units: String? = null,
        @Query("apikey") apiKey: String = BuildConfig.WEATHER_API_KEY
    ): Response<WeatherResponse>


}