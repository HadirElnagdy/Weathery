package com.example.weathery.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weathery.data.models.Alert
import com.example.weathery.data.models.FavLocationsWeather
import com.example.weathery.data.utils.WeatherResponseConverter

@Database(entities = arrayOf(Alert::class), version = 1 )
abstract class AlertsDatabase: RoomDatabase() {
    abstract fun getAlertsDao(): AlertsDao
    companion object{
        @Volatile
        private var INSTANCE: AlertsDatabase? = null
        fun getInstance (context: Context): AlertsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext, AlertsDatabase::class.java,
                    "alerts_database")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
