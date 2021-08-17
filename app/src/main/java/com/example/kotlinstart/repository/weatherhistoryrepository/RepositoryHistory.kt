package com.example.kotlinstart.repository.weatherhistoryrepository

import com.example.kotlinstart.room.HistoryEntity

interface RepositoryHistory {
    fun getWeatherDataFromLocalStorage(): List<HistoryEntity>
}