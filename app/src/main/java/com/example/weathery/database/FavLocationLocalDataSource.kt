/*
package com.example.weathery.database

import android.content.Context
import com.example.weathery.models.FavLocationsWeather
import kotlinx.coroutines.flow.StateFlow

interface FavLocationLocalDataSource{
    fun getAllFavorites(): StateFlow<List<FavLocationsWeather>>
    suspend fun insertFavorite(favorite: FavLocationsWeather)
    suspend fun updateFavorite(favorite: FavLocationsWeather)
    suspend fun deleteFavorite(favorite: FavLocationsWeather)
}

class FavLocationLocalDataSourceImpl private constructor(context: Context): FavLocationLocalDataSource {
    companion object{
        private var instance: FavLocationLocalDataSourceImpl? = null
        fun getInstance (context: Context): FavLocationLocalDataSourceImpl {
            return instance?: synchronized(this){
                val temp = FavLocationLocalDataSourceImpl(context)
                instance = temp
                temp
            }
        }
    }
    val favLocationDao : FavLocationDao by lazy {
        WeatherDatabase.getInstance(context).getFavDao()
    }


    override fun getAllFavorites(): StateFlow<List<FavLocationsWeather>> {
        return favLocationDao.getAllFavorites()
    }

    override suspend fun insertFavorite(favorite: FavLocationsWeather) {
        favLocationDao.insertFavorite(favorite)
    }

    override suspend fun updateFavorite(favorite: FavLocationsWeather) {
        favLocationDao.updateFavorite(favorite)
    }

    override suspend fun deleteFavorite(favorite: FavLocationsWeather) {
        favLocationDao.deleteFavorite(favorite)
    }
}*/
