package com.example.kotlinstart.repository.detailsrepository

import com.example.kotlinstart.repository.loader.getDetailWeather
import com.example.kotlinstart.repository.loader.getWeatherFromServer
import okhttp3.Callback

class RepositoryDetailsImpl : RepositoryDetails {

    override fun getWeatherDataFromServer(city: String, callback: Callback) =
        getWeatherFromServer(city, callback)

    override fun getWeatherDataFromLocalStorage(city: String) = getDetailWeather(city)
}
