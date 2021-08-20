package com.example.kotlinstart.repository.detailsrepository.datasource

import com.example.kotlinstart.BuildConfig
import com.example.kotlinstart.KotlinStartApplication.Companion.getWeatherAPI
import com.example.kotlinstart.dto.WeatherDTO
import retrofit2.Callback

class RemoteDataSource {

    fun getWeatherDetails(lat: Double, lon: Double, callback: Callback<WeatherDTO>) {
        getWeatherAPI().getWeather(BuildConfig.WEATHER_API_KEY, lat, lon).enqueue(callback)
    }
}