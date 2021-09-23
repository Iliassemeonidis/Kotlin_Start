package com.example.kotlinstart

import com.example.kotlinstart.model.WeatherParams

interface GeolocationInterface : DialogInterface {
    fun getRequestPermissionRationale(): Boolean
    fun getWeatherParamsFromUserLocation(params: WeatherParams)
}