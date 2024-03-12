package com.example.weathery.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.weathery.utils.WeatherResponseConverter
import org.jetbrains.annotations.Nullable

@Entity(tableName = "favorite_weather_table")
@TypeConverters(WeatherResponseConverter::class)
data class FavLocationsWeather(
    @PrimaryKey
    val locality: String,
    val forecast: WeatherResponse? = null)
