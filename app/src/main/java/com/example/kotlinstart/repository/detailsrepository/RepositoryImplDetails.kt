package com.example.kotlinstart.repository.detailsrepository

import com.example.kotlinstart.model.getDetailWeather

internal class RepositoryImplDetails : RepositoryDetails {

    override fun getWeatherDataFromLocalStorage(city: String) = getDetailWeather(city = city)

}
