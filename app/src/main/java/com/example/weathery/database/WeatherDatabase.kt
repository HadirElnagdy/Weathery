/*
package com.example.weathery.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.weathery.models.FavLocationsWeather

@Database(entities = arrayOf(FavLocationsWeather::class), version = 1 )
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun getFavDao(): FavLocationDao
    companion object{
        @Volatile
        private var INSTANCE: WeatherDatabase? = null
        fun getInstance (context: Context): WeatherDatabase{
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, WeatherDatabase::class.java,
                    "weather_database")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}
*/
