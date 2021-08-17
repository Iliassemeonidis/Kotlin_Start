package com.example.kotlinstart.repository.detailsrepository.datasource

import com.example.kotlinstart.KotlinStartApplication.Companion.getHistoryDao

class LocalDataSource {

    private val weatherHistoryDao = getHistoryDao()

    fun getWeatherHistoryDao() = weatherHistoryDao.all()

}
