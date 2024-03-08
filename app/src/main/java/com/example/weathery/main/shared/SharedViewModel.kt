package com.example.weathery.main.shared

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weathery.models.WeatherRepository
import com.example.weathery.utils.ApiState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.util.concurrent.TimeUnit
import com.google.android.gms.location.*

class SharedViewModel(private val app: Application, private val repository: WeatherRepository) : ViewModel() {


    private lateinit var location: Pair<Double, Double> //lon, lat
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(app)
    }

    private lateinit var locationCallback: LocationCallback
    private var locationListener: ((Pair<Double, Double>) -> Unit)? = null
    private var _mutableForecast = MutableStateFlow<ApiState>(ApiState.Loading)
    val forecast = _mutableForecast.asStateFlow()


    fun addToFav(address: String) {
        viewModelScope.launch(Dispatchers.IO) {
//            repository.addToFav(address)
        }
    }

    fun delFromFav(address: String){
        viewModelScope.launch(Dispatchers.IO) {
//            repository.delFromFav(address)
        }
    }

    fun getForcast(lon: Double, lat: Double) = viewModelScope.launch(Dispatchers.IO) {

        repository.getWeatherForecast(lon, lat)
            .catch {
                _mutableForecast.value = ApiState.Failure(it)
            }
            .collect {
                _mutableForecast.value = ApiState.Success(it)
            }
    }

    fun getLocationWithPermissions(onLocationReceived: (Pair<Double, Double>) -> Unit) {
        this.locationListener = onLocationReceived
        requestLocationPermissions()
    }

    private fun requestLocationPermissions() {
        if (checkPermissions()) {
            if(isLocationEnabled()){
                requestNewLocationData()
            }
        } else {

        }
    }

    @SuppressLint("MissingPermission")

    private fun requestNewLocationData(){
        // Permissions are granted, request location updates
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = TimeUnit.SECONDS.toMillis(10) // Update interval in milliseconds
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val locationPair = Pair(latitude, longitude)
                    locationListener?.invoke(locationPair)
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    private fun isLocationEnabled(): Boolean {
        /*val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)*/
        return true
    }

    private fun checkPermissions() = ActivityCompat.checkSelfPermission(app, ACCESS_COARSE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(app, ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED


    private fun getAddress(lat: Double, lng: Double): String {
        val geocoder = Geocoder(app)
        val list = geocoder.getFromLocation(lat, lng, 1)
        return list?.get(0)?.getAddressLine(0) ?: "0"
    }

}


class SharedViewModelFactory(private val app: Application, private val repo: WeatherRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SharedViewModel::class.java)){
            SharedViewModel(app, repo) as T
        }else{
            throw IllegalArgumentException("View Model is not found")
        }
    }
}