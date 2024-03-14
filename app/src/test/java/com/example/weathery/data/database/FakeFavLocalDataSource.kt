package com.example.weathery.data.database

import com.example.weathery.data.models.FavLocationsWeather
import kotlinx.coroutines.flow.flow

class FakeFavLocalDataSource(): FavLocationLocalDataSource {

    private val favorites: MutableList<FavLocationsWeather> = mutableListOf()
    override fun getAllFavorites() = flow {
            emit(favorites)
    }

    override suspend fun insertFavorite(favorite: FavLocationsWeather) {
        if(!favorites.contains(favorite))
            favorites.add(favorite)
    }

    override suspend fun deleteFavorite(favorite: FavLocationsWeather) {
        favorites.remove(favorite)
    }

}