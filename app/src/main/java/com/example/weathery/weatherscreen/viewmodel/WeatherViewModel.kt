package com.example.weathery.weatherscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathery.models.WeatherRepository
import com.example.weathery.utils.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private var mutableStateFlow = MutableStateFlow<ApiState>(ApiState.Loading)
    val stateFlow = mutableStateFlow.asStateFlow()


    fun addToFav(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
//            repository.addProductToFav(city)
        }
    }

    fun getForcast(lon: Double, lat: Double) = viewModelScope.launch(Dispatchers.IO) {
        repository.getWeatherForecast(lon, lat)
            .catch {
                mutableStateFlow.value = ApiState.Failure(it)
            }
            .collect {
                mutableStateFlow.value = ApiState.Success(it)
            }
    }


}