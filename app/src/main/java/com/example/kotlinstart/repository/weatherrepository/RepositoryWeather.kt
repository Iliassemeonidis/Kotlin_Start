package com.example.kotlinstart.repository.weatherrepository

import com.example.kotlinstart.model.Weather

interface RepositoryWeather {

    fun getListCityWeatherFromLocalStorage(): ArrayList<Weather>
}
