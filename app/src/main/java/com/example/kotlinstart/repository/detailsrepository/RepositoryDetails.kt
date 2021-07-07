package com.example.kotlinstart.repository.detailsrepository

import com.example.kotlinstart.repository.loader.Loader

interface RepositoryDetails {
    fun getWeatherDataFromLocalStorage(city: String, listener: Loader.OnWeatherListener)
}
