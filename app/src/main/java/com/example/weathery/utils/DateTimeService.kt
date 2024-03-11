package com.example.weathery.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class DateTimeService {
    companion object{
        val currentDateTime: String
            get() {
                val formatter = SimpleDateFormat("MMM dd hh:mm a"/*, Locale("en")*/)
                val currentDate = Date()
                return formatter.format(currentDate)
            }
    }

}