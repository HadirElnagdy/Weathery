package com.example.weathery.network

import com.example.weathery.models.WeatherResponse
import com.example.weathery.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("onecall")
    suspend fun getWeatherForecast(
        @Query("lon") lon: Double,
        @Query("lat") lat: Double,
        @Query("apikey") apiKey: String = API_KEY
    ): Response<WeatherResponse>


}