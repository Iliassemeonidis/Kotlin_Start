package com.example.kotlinstart.view.base.baseinterface

import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.model.WeatherParams

interface OnGetAddressListener {
    fun onValidData(weather: WeatherParams)
    fun onError(error: Throwable)
    fun onEmpty()
}