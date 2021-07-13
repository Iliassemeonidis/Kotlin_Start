package com.example.kotlinstart.repository.detailsrepository

import com.example.kotlinstart.dto.WeatherDTO
import com.example.kotlinstart.repository.loader.RemoteDataSource
import com.example.kotlinstart.model.getDetailWeather

class RepositoryDetailsImpl(private val remoteDataSource: RemoteDataSource) : RepositoryDetails {

    override fun getWeatherDataFromServers(
        lat: Double,
        lon: Double,
        callback: retrofit2.Callback<WeatherDTO>
    ) {
        remoteDataSource.getWeatherDetails(lat, lon, callback)
    }

    override fun getWeatherDataFromLocalStorage(city: String) = getDetailWeather(city)
}
