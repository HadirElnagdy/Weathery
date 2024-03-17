package com.example.weathery.data.database

import android.content.Context
import com.example.weathery.data.models.FavLocationsWeather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface FavLocationLocalDataSource{
    fun getHome(): Flow<FavLocationsWeather>
    fun getAllFavorites(): Flow<List<FavLocationsWeather>>
    suspend fun insertFavorite(favorite: FavLocationsWeather)
    suspend fun deleteFavorite(favorite: FavLocationsWeather)
}

class FavLocationLocalDataSourceImpl private constructor(context: Context):
    FavLocationLocalDataSource {
    companion object{
        private var instance: FavLocationLocalDataSourceImpl? = null
        fun getInstance (context: Context): FavLocationLocalDataSourceImpl {
            return instance ?: synchronized(this){
                val temp = FavLocationLocalDataSourceImpl(context)
                instance = temp
                temp
            }
        }
    }
    val favLocationDao : FavLocationDao by lazy {
        WeatherDatabase.getInstance(context).getFavDao()
    }

    override fun getHome(): Flow<FavLocationsWeather> = favLocationDao.getAllFavorites().map { favorites ->
        favorites.filter { it.forecast?.current?.uvi == "Home" }.get(0)
    }


    override fun getAllFavorites(): Flow<List<FavLocationsWeather>> = favLocationDao.getAllFavorites().map { favorites ->
        favorites.filter { it.forecast?.current?.uvi != "Home" }
    }


    override suspend fun insertFavorite(favorite: FavLocationsWeather) {
        favLocationDao.insertFavorite(favorite)
    }

    override suspend fun deleteFavorite(favorite: FavLocationsWeather) {
        favLocationDao.deleteFavorite(favorite)
    }
}
