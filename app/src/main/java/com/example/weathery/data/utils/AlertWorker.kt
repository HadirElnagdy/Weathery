package com.example.weathery.data.utils

import android.app.Application
import android.content.Context
import android.graphics.PixelFormat
import android.location.Location
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.lifecycleScope
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weathery.R
import com.example.weathery.data.database.AlertsLocalDataSourceImpl
import com.example.weathery.data.database.FavLocationLocalDataSourceImpl
import com.example.weathery.data.models.Alert
import com.example.weathery.data.models.FavLocationsWeather
import com.example.weathery.data.models.WeatherResponse
import com.example.weathery.data.network.WeatherRemoteDataSourceImpl
import com.example.weathery.data.repositories.AlertsRepository
import com.example.weathery.data.repositories.AlertsRepositoryImpl
import com.example.weathery.data.repositories.WeatherRepositoryImpl
import com.example.weathery.main.shared.WeatherViewModel
import com.example.weathery.main.shared.WeatherViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlertWorker(private val context: Context, workerParams: WorkerParameters)
    : CoroutineWorker(context, workerParams) {

    private val alertRepo: AlertsRepository = AlertsRepositoryImpl.getInstance(
        AlertsLocalDataSourceImpl.getInstance(context)
    )

    private val weatherViewModel: WeatherViewModel by lazy {
        ViewModelProvider(context as ViewModelStoreOwner, WeatherViewModelFactory(
            context.applicationContext as Application,
            WeatherRepositoryImpl.getInstance(
                FavLocationLocalDataSourceImpl.getInstance(context),
                WeatherRemoteDataSourceImpl
            )
        ))[WeatherViewModel::class.java]
    }

    override suspend fun doWork(): Result {
        val alertId = inputData.getLong("id", -1L)
        if (alertId == -1L) {
            return Result.failure()
        }
        return try {
            val alert = withContext(Dispatchers.IO) { alertRepo.getAlertWithId(alertId) }
            val forecast = weatherViewModel.forecast.firstOrNull()
            val weatherResponse = (forecast as? ApiState.Success)?.data
            weatherResponse?.let { createAlarm(context, alert, it) }
            Result.success()
        } catch (e: Exception) {
            Log.e("AlertWorker", "Error processing work: $e")
            Result.failure()
        }
    }


    private suspend fun createAlarm(context: Context, message: Alert, weatherForecast: WeatherResponse) {
        val mediaPlayer = MediaPlayer.create(context, R.raw.alarm)
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.alarm_layout, null, false)
        val dismissButton = view.findViewById<Button>(R.id.btn_dismiss)
        val desc = view.findViewById<TextView>(R.id.txt_alert_desc)
        desc.text = weatherForecast.alert?.description
        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY else WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        layoutParams.gravity = Gravity.TOP

        val windowManager =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        withContext(Dispatchers.Main) {
            windowManager.addView(view, layoutParams)
            view.visibility = View.VISIBLE

        }
        mediaPlayer.start()
        mediaPlayer.isLooping = true
        dismissButton.setOnClickListener {
            mediaPlayer?.release()
            windowManager.removeView(view)
        }
    }

}
