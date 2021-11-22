package com.example.kotlinstart.view.detailsscreen

import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.model.WeatherParams

sealed class SearchCityState {
    data class Success(val weather: WeatherParams) : SearchCityState()
    object Empty : SearchCityState()
    data class Error(val error: Throwable) : SearchCityState()
}

