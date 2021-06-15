package com.example.kotlinstart.repository

import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.model.getCityWeather
import com.example.kotlinstart.model.getDetailWeather
import com.example.kotlinstart.model.getListOfCityNames

internal class RepositoryImpl : Repository {

    override fun getWeatherFromServer() = Weather()
    override fun getListCityWeatherFromLocalStorage() = getCityWeather()
    override fun getListCityNamesFromLocalStorage() = getListOfCityNames()
    override fun getWeatherDataFromLocalStorage(city: String) = getDetailWeather(city = city)

}
