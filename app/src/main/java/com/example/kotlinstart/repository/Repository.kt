package com.example.kotlinstart.repository

import com.example.kotlinstart.view.data.CityData
import com.example.kotlinstart.view.data.Weather
import com.example.kotlinstart.view.data.WeatherData

interface Repository {

    fun getWeatherFromServer(): Weather
    fun getWeatherDataFromLocalStorage(city:String): WeatherData
    fun getListCityWeatherFromLocalStorage(): ArrayList<Weather>
    fun getListCityNamesFromLocalStorage():ArrayList<CityData>
}
