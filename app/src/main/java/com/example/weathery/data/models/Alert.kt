package com.example.weathery.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "alerts_table")
data class Alert (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val endDate: String,
    val startTime: String,
    val endTime: String,
    val alertType: String
    ): Serializable