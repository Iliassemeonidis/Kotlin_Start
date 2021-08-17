package com.example.kotlinstart.repository.weatherrepository

import com.example.kotlinstart.model.getCityWeather

class RepositoryWeatherImpl : RepositoryWeather {
    override fun getListCityWeatherFromLocalStorage() = getCityWeather()
}
