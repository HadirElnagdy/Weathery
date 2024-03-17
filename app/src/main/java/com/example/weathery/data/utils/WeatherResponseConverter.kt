package com.example.weathery.data.utils

import androidx.room.TypeConverter
import com.example.weathery.data.models.WeatherResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WeatherResponseConverter {
    @TypeConverter
    fun fromWeatherResponse(weatherResponse: WeatherResponse?): String {
        return Gson().toJson(weatherResponse)
    }

    @TypeConverter
    fun toWeatherResponse(weatherResponseString: String?): WeatherResponse? {
        if (weatherResponseString == null) {
            return null
        }
        return Gson().fromJson(weatherResponseString, WeatherResponse::class.java)
    }
}