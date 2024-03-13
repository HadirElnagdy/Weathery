package com.example.weathery.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.weathery.data.models.FavLocationsWeather
import kotlinx.coroutines.flow.Flow

@Dao
interface FavLocationDao {

    @Query("SELECT * FROM favorite_weather_table")
    fun getAllFavorites(): Flow<List<FavLocationsWeather>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: FavLocationsWeather)

    @Delete
    suspend fun deleteFavorite(favorite: FavLocationsWeather)
}
