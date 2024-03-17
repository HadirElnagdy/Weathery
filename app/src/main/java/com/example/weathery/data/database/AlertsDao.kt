package com.example.weathery.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weathery.data.models.Alert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertsDao {
    @Query("select * from alerts_table")
    fun getListOfAlerts(): Flow<List<Alert>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alert: Alert): Long
    @Delete
    suspend fun deleteAlert(alert: Alert)
    @Query("select * from alerts_table where id = :id")
    fun getAlertWithId(id: Long): Alert
}