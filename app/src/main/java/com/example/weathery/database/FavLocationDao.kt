package com.example.weathery.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.weathery.models.FavLocationsWeather
import kotlinx.coroutines.flow.Flow

@Dao
interface FavLocationDao {

    @Query("SELECT * FROM favorite_weather_table")
    fun getAllFavorites(): Flow<List<FavLocationsWeather>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavLocationsWeather)

    @Update
    suspend fun updateFavorite(favorite: FavLocationsWeather)

    @Delete
    suspend fun deleteFavorite(favorite: FavLocationsWeather)
}
