package com.example.weathery.data.repositories

import com.example.weathery.data.database.AlertsLocalDataSource
import com.example.weathery.data.models.Alert
import com.example.weathery.data.sharedpreferences.SettingsLocalDataSource
import kotlinx.coroutines.flow.Flow

interface AlertsRepository {
    suspend fun insertAlert(alert: Alert): Long
    suspend fun deleteAlert(alert: Alert)
    fun getListOfAlerts(): Flow<List<Alert>>
    fun getAlertWithId(id: Long): Alert
}

class AlertsRepositoryImpl private constructor(private val localDataSource: AlertsLocalDataSource): AlertsRepository{

    companion object{
        private var instance: AlertsRepositoryImpl? = null
        fun getInstance (localDataSource: AlertsLocalDataSource
        ): AlertsRepositoryImpl {
            return instance ?: synchronized(this){
                val temp = AlertsRepositoryImpl(localDataSource)
                instance = temp
                temp
            }
        }
    }

    override suspend fun insertAlert(alert: Alert) = localDataSource.insertAlert(alert)

    override suspend fun deleteAlert(alert: Alert) = localDataSource.deleteAlert(alert)

    override fun getListOfAlerts(): Flow<List<Alert>> = localDataSource.getListOfAlerts()

    override fun getAlertWithId(id: Long): Alert = localDataSource.getAlertWithId(id)

}