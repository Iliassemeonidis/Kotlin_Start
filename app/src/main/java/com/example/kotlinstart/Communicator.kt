package com.example.kotlinstart

import com.example.kotlinstart.weather.Weather

internal interface Communicator {

    fun passDataComm(weather: Weather)
}
