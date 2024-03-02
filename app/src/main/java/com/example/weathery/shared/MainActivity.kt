package com.example.weathery.shared

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.weathery.R
import com.example.weathery.network.WeatherRemoteDataSourceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycleScope.launch (Dispatchers.IO){
            var response = WeatherRemoteDataSourceImpl.getWeatherForecast(123.456, 78.90)
            withContext(Dispatchers.Main) {
                Log.i(TAG, "onCreate: " + response.current)
            }
        }


    }
}