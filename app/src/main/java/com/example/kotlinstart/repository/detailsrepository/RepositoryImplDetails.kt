package com.example.kotlinstart.repository.detailsrepository

import com.example.kotlinstart.model.getDetailWeather

class RepositoryImplDetails : RepositoryDetails {

    override fun getWeatherDataFromLocalStorage(city: String) = getDetailWeather(city = city)

}
