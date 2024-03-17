package com.example.weathery.main.shared

import android.app.Application
import android.location.Geocoder
import android.location.Location
import androidx.core.content.ContextCompat.getString
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weathery.R
import com.example.weathery.data.models.FavLocationsWeather
import com.example.weathery.data.repositories.WeatherRepository
import com.example.weathery.data.utils.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.IOException
import java.lang.IllegalArgumentException

class WeatherViewModel(private val app: Application, private val repository: WeatherRepository) : ViewModel() {

    private var _mutableForecast = MutableStateFlow<ApiState>(ApiState.Loading)
    val forecast = _mutableForecast.asStateFlow()

    private var _mutableSavedForecast = MutableLiveData<FavLocationsWeather>()
    val savedHomeForecast: LiveData<FavLocationsWeather> = _mutableSavedForecast

    private var _mutableFavForecast = MutableStateFlow<ApiState>(ApiState.Loading)
    val favForecast = _mutableFavForecast.asStateFlow()

    private var _mutableFavList = MutableLiveData<List<FavLocationsWeather>>()
    val favList: LiveData<List<FavLocationsWeather>> = _mutableFavList

    var homeLocation: Location? = null
    var showFav = false

    fun getAllFavs() = viewModelScope.launch(Dispatchers.IO) {
        repository.getAllFavorites().collectLatest{
            _mutableFavList.postValue(it)
        }
    }


    fun addToFav(location: Location) {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherResponse = repository.getWeatherForecast(location.longitude, location.latitude)
            weatherResponse.collectLatest {
                val fav = FavLocationsWeather(getAdminArea(location), it)
                repository.insertFavorite(fav)
            }
        }
    }

    fun delFromFav(fav: FavLocationsWeather){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFavorite(fav)
        }
    }

    fun getForecast(location: Location?,
                    language: String? = null,
                    units: String? = null) = viewModelScope.launch(Dispatchers.IO) {
        location?.let {
            repository.getWeatherForecast(it.longitude, it.latitude, lang = language, units = units)
                .catch {
                    _mutableForecast.value = ApiState.Failure(it)
                }
                .collect {
                    _mutableForecast.value = ApiState.Success(it)
                }
        }
    }

    fun getFavForecast(location: Location?,
                       language: String? = null,
                       units: String? = null) = viewModelScope.launch(Dispatchers.IO) {
        location?.let {
            repository.getWeatherForecast(it.longitude, it.latitude, lang = language, units = units)
                .catch {
                    _mutableFavForecast.value = ApiState.Failure(it)
                }
                .collect {
                    _mutableFavForecast.value = ApiState.Success(it)
                }
        }

    }

    fun saveHomeForecast(homeForecast: FavLocationsWeather){
        homeForecast.forecast?.current?.uvi = "Home"
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertFavorite(homeForecast)
        }
    }

    fun getHomeFromDatabase() = viewModelScope.launch(Dispatchers.IO) {
        repository.getHome().collectLatest {
            _mutableSavedForecast.postValue(it)
        }
    }


    fun getAdminArea(location: Location?): String {
        val geocoder = Geocoder(app/*, Locale("ar")*/)
        return try {
            val list = geocoder.getFromLocation(location?.latitude ?: 0.0, location?.longitude ?: 0.0, 1)
            val adminArea = list?.getOrNull(0)?.adminArea
            val addressLine = list?.getOrNull(0)?.getAddressLine(0)
            val countryName = list?.getOrNull(0)?.countryName
            adminArea ?: getString(app, R.string.unknown)
        } catch (e: IOException) {
            getString(app, R.string.unknown)
        }
    }


}

class WeatherViewModelFactory(private val app: Application, private val repo: WeatherRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WeatherViewModel::class.java)){
            WeatherViewModel(app, repo) as T
        }else{
            throw IllegalArgumentException("View Model is not found")
        }
    }
}

