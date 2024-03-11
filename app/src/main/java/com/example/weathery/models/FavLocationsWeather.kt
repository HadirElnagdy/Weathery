package com.example.weathery.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_weather_table")
data class FavLocationsWeather(
    @PrimaryKey
    val locality: String, val forecast: WeatherResponse)
