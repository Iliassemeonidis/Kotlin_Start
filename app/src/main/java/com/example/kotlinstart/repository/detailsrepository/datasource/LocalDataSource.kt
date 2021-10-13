package com.example.kotlinstart.repository.detailsrepository.datasource

import com.example.kotlinstart.KotlinStartApplication.Companion.getHistoryDao

class LocalDataSource {
    fun getWeatherHistory() = getHistoryDao().all()

}
