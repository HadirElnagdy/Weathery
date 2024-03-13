package com.example.weathery.utils

import com.example.weathery.data.models.WeatherResponse


sealed class ApiState {
    class Success(val data: WeatherResponse): ApiState()
    class Failure(val msg: Throwable): ApiState()
    object Loading: ApiState()

}