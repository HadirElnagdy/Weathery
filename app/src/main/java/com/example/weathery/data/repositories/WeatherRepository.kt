package com.example.weathery.data.repositories

import com.example.weathery.data.database.FavLocationLocalDataSource
import com.example.weathery.data.models.FavLocationsWeather
import com.example.weathery.data.models.WeatherResponse
import com.example.weathery.data.network.RetrofitHelper
import com.example.weathery.data.network.WeatherRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

interface WeatherRepository {
    suspend fun getWeatherForecast(lon: Double, lat: Double, lang: String? = null, units: String? = null) : Flow<WeatherResponse>
    fun getAllFavorites(): Flow<List<FavLocationsWeather>>
    suspend fun insertFavorite(favorite: FavLocationsWeather)
    suspend fun deleteFavorite(favorite: FavLocationsWeather)
    fun getHome(): Flow<FavLocationsWeather>

}

class WeatherRepositoryImpl private constructor(private val localDataSource: FavLocationLocalDataSource,
    private val remoteDataSource: WeatherRemoteDataSource):
    WeatherRepository {

    companion object{
        private var instance: WeatherRepositoryImpl? = null
        fun getInstance (localDataSource: FavLocationLocalDataSource,
                         remoteDataSource: WeatherRemoteDataSource): WeatherRepositoryImpl {
            return instance ?: synchronized(this){
                val temp = WeatherRepositoryImpl(localDataSource, remoteDataSource)
                instance = temp
                temp
            }
        }
    }
    override suspend fun getWeatherForecast(lon: Double,
                                            lat: Double,
                                            lang: String?,
                                            units: String?) = remoteDataSource
                                                .getWeatherForecast(lon, lat, lang, units)

    override fun getAllFavorites(): Flow<List<FavLocationsWeather>> = localDataSource.getAllFavorites()

    override suspend fun insertFavorite(favorite: FavLocationsWeather) = localDataSource.insertFavorite(favorite)

    override suspend fun deleteFavorite(favorite: FavLocationsWeather) = localDataSource.deleteFavorite(favorite)
    override fun getHome(): Flow<FavLocationsWeather> = localDataSource.getHome()


}