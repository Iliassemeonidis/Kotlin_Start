package com.example.kotlinstart.view

import com.example.kotlinstart.view.data.Weather

internal interface Communicator {

    fun openWeatherDetails(weather: Weather)
}
