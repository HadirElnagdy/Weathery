package com.example.weathery.data.repositories

import com.example.weathery.data.models.FavLocationsWeather
import com.example.weathery.data.models.WeatherResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeWeatherRepository: WeatherRepository{

    var favoriteList: List<FavLocationsWeather> = mutableListOf()

    override suspend fun getWeatherForecast(
        lon: Double,
        lat: Double,
        lang: String?,
        units: String?
    ): Flow<WeatherResponse> = flow{
        emit(WeatherResponse(lon = lon, lat = lat))
    }

    override fun getAllFavorites(): Flow<List<FavLocationsWeather>> = flow{
        emit(favoriteList)
    }

    override suspend fun insertFavorite(favorite: FavLocationsWeather) {
        favoriteList += favorite
    }

    override suspend fun deleteFavorite(favorite: FavLocationsWeather) {
        favoriteList -= favorite
    }

}