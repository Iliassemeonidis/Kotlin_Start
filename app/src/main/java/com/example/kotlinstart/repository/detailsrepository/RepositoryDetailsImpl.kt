package com.example.kotlinstart.repository.detailsrepository

import com.example.kotlinstart.dto.WeatherDTO
import com.example.kotlinstart.repository.detailsrepository.datasource.LocalDataSource
import com.example.kotlinstart.repository.detailsrepository.datasource.RemoteDataSource
import com.example.kotlinstart.room.HistoryEntity
import retrofit2.Callback

class RepositoryDetailsImpl (private val remoteDataSource: RemoteDataSource, private val localDataSource: LocalDataSource) : RepositoryDetails {

    override fun getWeatherDataFromServers(
        lat: Double,
        lon: Double,
        callback: Callback<WeatherDTO>
    ) {
        remoteDataSource.getWeatherDetails(lat, lon, callback)
    }

    override fun getWeatherDataFromLocalStorage(): List<HistoryEntity> {
        TODO("Not yet implemented")
    }
}
