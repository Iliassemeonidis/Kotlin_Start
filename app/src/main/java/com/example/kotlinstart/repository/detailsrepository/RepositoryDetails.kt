package com.example.kotlinstart.repository.detailsrepository

import com.example.kotlinstart.model.WeatherData

interface RepositoryDetails {
    fun getWeatherDataFromLocalStorage(city: String) : WeatherData
}
