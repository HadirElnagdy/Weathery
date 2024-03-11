package com.example.weathery.main.shared

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weathery.models.WeatherRepository
import com.example.weathery.utils.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import java.util.Locale

class SharedViewModel(private val context: Context, private val repository: WeatherRepository) : ViewModel() {

    private var _mutableForecast = MutableStateFlow<ApiState>(ApiState.Loading)
    val forecast = _mutableForecast.asStateFlow()
    var location: Location? = null
    private val TAG = "SharedViewModel"

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

    fun getForecast(location: Location?) = viewModelScope.launch(Dispatchers.IO) {
        Log.i(TAG, "getForecast: ${location.toString()}")
        this@SharedViewModel.location = location
        location?.let {
            repository.getWeatherForecast(it.longitude, it.latitude)
                .catch {
                    _mutableForecast.value = ApiState.Failure(it)
                }
                .collect {
                    _mutableForecast.value = ApiState.Success(it)
                }
        }
    }

    fun getCityName(): String {
        val geocoder = Geocoder(context/*, Locale("ar")*/)
        val list = geocoder.getFromLocation(location!!.latitude, location!!.longitude, 1)
        return list?.get(0)?.locality ?: "0"
    }

}

class SharedViewModelFactory(private val context: Context, private val repo: WeatherRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SharedViewModel::class.java)){
            SharedViewModel(context, repo) as T
        }else{
            throw IllegalArgumentException("View Model is not found")
        }
    }
}