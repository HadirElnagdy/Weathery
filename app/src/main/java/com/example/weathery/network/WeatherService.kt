package com.example.weathery.network

import com.example.weathery.models.WeatherResponse
import com.example.weathery.BuildConfig
import com.example.weathery.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("onecall")
    suspend fun getWeatherForecast(
        @Query("lon") lon: Double,
        @Query("lat") lat: Double,
        @Query("lang") lang: String? = null,
        @Query("units") units: String? = Constants.UNITS_METRIC_KEY,
        @Query("apikey") apiKey: String = BuildConfig.WEATHER_API_KEY
    ): Response<WeatherResponse>


}