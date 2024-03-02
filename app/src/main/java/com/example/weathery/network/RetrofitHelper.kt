package com.example.weathery.network

import com.example.weathery.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    val weatherRetrofitInstance = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Constants.WEATHER_BASE_URL)
        .build()


    val retrofitWeatherService: WeatherService by lazy {
        RetrofitHelper.weatherRetrofitInstance.create(WeatherService::class.java)
    }

}