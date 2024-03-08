package com.example.weathery.models

import android.location.Location
import com.google.android.gms.location.LocationCallback

interface LocationRepository {
    fun getLastKnownLocation(): Location?
    suspend fun requestLocationUpdates(callback: LocationCallback)
    suspend fun removeLocationUpdates(callback: LocationCallback)
}
