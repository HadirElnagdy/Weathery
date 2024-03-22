package com.example.weathery.data.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import kotlin.math.log

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val alertId = intent.getLongExtra("id", -1L)
        Log.i("TAG", "onReceive: ${alertId}")
        val data = Data.Builder().putLong("id", alertId).build()
        val alertWork = OneTimeWorkRequest.Builder(AlertWorker::class.java)
            .setInputData(data)
            .build()
        WorkManager.getInstance(context).enqueue(alertWork)
    }

}