package com.example.kotlinstart.repository.detailsrepository

import com.example.kotlinstart.dto.WeatherDTO
import com.example.kotlinstart.room.HistoryEntity

interface RepositoryDetails {

    fun getWeatherDataFromServers(
        lat: Double,
        lon: Double,
        callback: retrofit2.Callback<WeatherDTO>
    )
}
