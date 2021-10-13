package com.example.kotlinstart.view.main

import com.example.kotlinstart.model.WeatherParams

interface OnGetAddressListener {
    fun onValidData(weatherParams: WeatherParams)
    fun onError()
}