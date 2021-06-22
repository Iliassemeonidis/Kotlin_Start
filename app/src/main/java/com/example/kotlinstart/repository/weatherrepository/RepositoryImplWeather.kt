package com.example.kotlinstart.repository.weatherrepository

import com.example.kotlinstart.model.getCityWeather

internal class RepositoryImplWeather : RepositoryWeather {

    override fun getListCityWeatherFromLocalStorage() = getCityWeather()
}
