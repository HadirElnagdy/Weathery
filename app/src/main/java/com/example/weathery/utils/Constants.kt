package com.example.weathery.utils

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

class Constants {
    companion object{

        const val WEATHER_BASE_URL = "https://api.openweathermap.org/data/3.0/"
        const val PERMISSION_ID = 5005
        const val LANG_ENGLISH_KEY = "en"
        const val LANG_ARABIC_KEY = "ar"
        const val UNITS_IMPERIAL_KEY = "imperial"
        const val UNITS_METRIC_KEY = "metric"
        const val UNITS_STANDARD_KEY = "standard"
        const val KEY_LANGUAGE = "language"
        const val KEY_UNITS = "units"
        const val KEY_NOTIFICATION_ENABLED = "notification_enabled"


    }

}