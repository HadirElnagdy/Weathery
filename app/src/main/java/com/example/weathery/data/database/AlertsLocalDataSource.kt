package com.example.weathery.data.database

import android.content.Context
import com.example.weathery.data.models.Alert
import kotlinx.coroutines.flow.Flow

interface AlertsLocalDataSource {
    suspend fun insertAlert(alert: Alert)
    suspend fun deleteAlert(alert: Alert)
    fun getListOfAlerts(): Flow<List<Alert>>
    fun getAlertWithId(id: Long): Alert
}

class AlertsLocalDataSourceImpl private constructor(context: Context): AlertsLocalDataSource{
    companion object{
        private var instance: AlertsLocalDataSourceImpl? = null
        fun getInstance (context: Context): AlertsLocalDataSourceImpl {
            return instance ?: synchronized(this){
                val temp = AlertsLocalDataSourceImpl(context)
                instance = temp
                temp
            }
        }
    }
    val alertsDao : AlertsDao by lazy {
        AlertsDatabase.getInstance(context).getAlertsDao()
    }
    override suspend fun insertAlert(alert: Alert) {
        alertsDao.insertAlert(alert)
    }
    override suspend fun deleteAlert(alert: Alert) {
        alertsDao.deleteAlert(alert)
    }
    override fun getListOfAlerts(): Flow<List<Alert>> = alertsDao.getListOfAlerts()

    override fun getAlertWithId(id: Long) = alertsDao.getAlertWithId(id)

}