package com.example.weathery.utils

import com.example.weathery.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone


class SimpleUtils {
    companion object{
        fun convertUnixTimeStamp(unixTimestamp: Long?, timezoneId: String?): Pair<String, String> {
            val currentDateTime = "No timestamp provided" to "None"
            unixTimestamp?.let {
                val date = Date(unixTimestamp * 1000)
                val dateFormatter = SimpleDateFormat("MMM dd")
                val timeFormatter = SimpleDateFormat("hh:mm a")

                dateFormatter.timeZone = TimeZone.getTimeZone(timezoneId)
                timeFormatter.timeZone = TimeZone.getTimeZone(timezoneId)

                val formattedDate = dateFormatter.format(date)
                val formattedTime = timeFormatter.format(date)
                return Pair(formattedDate, formattedTime)
            }
            return currentDateTime
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

