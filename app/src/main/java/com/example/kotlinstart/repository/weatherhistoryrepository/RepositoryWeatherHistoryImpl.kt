package com.example.kotlinstart.repository.weatherhistoryrepository

import com.example.kotlinstart.repository.detailsrepository.datasource.LocalDataSource
import com.example.kotlinstart.room.HistoryEntity

class RepositoryWeatherHistoryImpl(private val localDataSource: LocalDataSource) :
    RepositoryHistory {

    override fun getWeatherDataFromLocalStorage() = localDataSource.getWeatherHistoryDao()

}