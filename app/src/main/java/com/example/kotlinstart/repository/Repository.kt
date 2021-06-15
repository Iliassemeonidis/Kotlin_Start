package com.example.kotlinstart.repository

import com.example.kotlinstart.model.CityData
import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.model.WeatherData

interface Repository {

    fun getWeatherFromServer(): Weather
    fun getWeatherDataFromLocalStorage(city:String): WeatherData
    fun getListCityWeatherFromLocalStorage(): ArrayList<Weather>
    fun getListCityNamesFromLocalStorage():ArrayList<CityData>
}
