package com.example.weathery.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class WeatherResponse (
    val current: Current? = null,
    val timezone: String? = null,
    val timezoneOffset: Int? = null,
    val daily: List<DailyItem?>? = null,
    val lon: Any? = null,
    val hourly: List<HourlyItem?>? = null,
    val minutely: List<MinutelyItem?>? = null,
    val lat: Any? = null
)

data class Current(
    val sunrise: Int? = null,
    val temp: Any? = "--",
    val visibility: Int? = null,
    val uvi: Any? = null,
    val pressure: Int? = null,
    val clouds: Int? = null,
    val feelsLike: Any? = null,
    val windGust: Any? = null,
    val dt: Int? = null,
    val windDeg: Int? = null,
    val dewPoint: Any? = null,
    val sunset: Int? = null,
    val weather: List<WeatherItem?>? = null,
    val humidity: Int? = null,
    val windSpeed: Any? = "--"
)

data class HourlyItem(
    val temp: Any? = null,
    val visibility: Int? = null,
    val uvi: Any? = null,
    val pressure: Int? = null,
    val clouds: Int? = null,
    val feelsLike: Any? = null,
    val windGust: Any? = null,
    val dt: Int? = null,
    val pop: Double? = null,
    val windDeg: Int? = null,
    val dewPoint: Any? = null,
    val weather: List<WeatherItem?>? = null,
    val humidity: Int? = null,
    val windSpeed: Any? = null
)

data class Temp(
    val min: Any? = null,
    val max: Any? = null,
    val eve: Any? = null,
    val night: Any? = null,
    val day: Any? = null,
    val morn: Any? = null
)

data class FeelsLike(
    val eve: Any? = null,
    val night: Any? = null,
    val day: Any? = null,
    val morn: Any? = null
)

data class MinutelyItem(
    val dt: Int? = null,
    val precipitation: Any? = null
)

data class WeatherItem(
    val icon: String? = null,
    val description: String? = null,
    val main: String? = null,
    val id: Int? = null
)

data class DailyItem(
    val moonset: Int? = null,
    val summary: String? = null,
    val rain: Any? = null,
    val sunrise: Int? = null,
    val temp: Temp? = null,
    val moonPhase: Any? = null,
    val uvi: Any? = null,
    val moonrise: Int? = null,
    val pressure: Int? = null,
    val clouds: Int? = null,
    val feelsLike: FeelsLike? = null,
    val windGust: Any? = null,
    val dt: Int? = null,
    val pop: Double? = null,
    val windDeg: Int? = null,
    val dewPoint: Any? = null,
    val sunset: Int? = null,
    val weather: List<WeatherItem?>? = null,
    val humidity: Int? = null,
    val windSpeed: Double? = null
)

