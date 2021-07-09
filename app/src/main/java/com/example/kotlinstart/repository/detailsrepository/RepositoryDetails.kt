package com.example.kotlinstart.repository.detailsrepository

import com.example.kotlinstart.model.WeatherData
import com.example.kotlinstart.repository.loader.Loader
import okhttp3.Callback

interface RepositoryDetails {
    fun getWeatherDataFromServer(city: String, callback: Callback)

    fun getWeatherDataFromLocalStorage(city: String):WeatherData
}
