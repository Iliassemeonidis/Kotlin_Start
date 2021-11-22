package com.example.kotlinstart.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val city: String,
    val temperature: String,
    val condition: String ,
    val lat: Double,
    val lon: Double
)