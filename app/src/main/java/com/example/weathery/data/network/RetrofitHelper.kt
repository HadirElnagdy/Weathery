package com.example.weathery.data.network

import com.example.weathery.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    val weatherRetrofitInstance = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Constants.WEATHER_BASE_URL)
        .build()


    val retrofitWeatherService: WeatherService by lazy {
        weatherRetrofitInstance.create(WeatherService::class.java)
    }


}