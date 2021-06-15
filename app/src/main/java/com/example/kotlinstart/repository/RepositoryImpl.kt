package com.example.kotlinstart.repository

import com.example.kotlinstart.view.data.*

internal class RepositoryImpl : Repository {

    override fun getWeatherFromServer() = Weather()
    override fun getListCityWeatherFromLocalStorage() = getCityWeather()
    override fun getListCityNamesFromLocalStorage() = getListOfCityNames()
    override fun getWeatherDataFromLocalStorage(city: String) = getDetailWeather(city = city)

}