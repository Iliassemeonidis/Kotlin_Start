package com.example.kotlinstart.repository.detailsrepository

import com.example.kotlinstart.model.WeatherData
import com.example.kotlinstart.repository.loader.Loader

interface RepositoryDetails {
    fun getWeatherDataFromServer(city: String, listener: Loader.OnWeatherListener)

    fun getWeatherDataFromLocalStorage(city: String):WeatherData
}
