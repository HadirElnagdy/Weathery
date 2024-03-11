package com.example.weathery.utils

import android.location.Location
import com.example.weathery.models.WeatherResponse

sealed class LocationState {
    class Success(val data: Location): LocationState()
    class Failure(val msg: String): LocationState()
    object Loading: LocationState()

}