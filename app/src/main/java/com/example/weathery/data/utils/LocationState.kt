package com.example.weathery.data.utils

import android.location.Location
import com.example.weathery.data.models.WeatherResponse

sealed class LocationState {
    class Success(val data: Location): LocationState()
    class Failure(val msg: String): LocationState()
    object Loading: LocationState()

}