package com.example.weathery.models

import android.location.Location
import com.google.android.gms.location.LocationCallback

object LocationRepositoryImpl: LocationRepository {
    override fun getLastKnownLocation(): Location? {
        TODO("Not yet implemented")
    }

    override suspend fun requestLocationUpdates(callback: LocationCallback) {
        TODO("Not yet implemented")
    }

    override suspend fun removeLocationUpdates(callback: LocationCallback) {
        TODO("Not yet implemented")
    }

}