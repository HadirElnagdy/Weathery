package com.example.weathery.splash

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import androidx.lifecycle.ViewModel
import com.example.weathery.utils.LocationState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.flow.MutableStateFlow

class SplashViewModel() : ViewModel() {

    val locationStateFlow = MutableStateFlow<LocationState>(LocationState.Loading)

    private val locationCallback: LocationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            val lastLocation = p0.lastLocation
            val location = lastLocation?.let { Location(lastLocation) }
            if(location != null){
                locationStateFlow.value = LocationState.Success(location)
            }else{
                locationStateFlow.value = LocationState.Failure("Location not found!")
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun getLocation(client: FusedLocationProviderClient){
        val locationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 0
        }
        client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }


}


