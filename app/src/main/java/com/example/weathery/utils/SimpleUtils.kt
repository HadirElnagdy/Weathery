package com.example.weathery.utils

import com.example.weathery.R
import java.text.SimpleDateFormat
import java.util.Date


class SimpleUtils {
    companion object{
        val currentDateTime: String
            get() {
                val formatter = SimpleDateFormat("MMM dd hh:mm a"/*, Locale("en")*/)
                val currentDate = Date()
                return formatter.format(currentDate)
            }

        fun getIconResourceId(iconCode: String): Int {
            return when (iconCode) {
                "01d" -> R.drawable.ic_sunny
                "01n" -> R.drawable.ic_clear_night
                "02d" -> R.drawable.ic_cloudy_day
                "02n" -> R.drawable.ic_clear_night
                "03d", "03n","04d", "04n" -> R.drawable.ic_cloud
                "09d", "09n", "10d", "10n" -> R.drawable.ic_rain
                "11d", "11n" -> R.drawable.ic_thunder
                "12d", "12n" -> R.drawable.ic_snow
                else -> R.drawable.ic_windy
            }
        }
    }

}

