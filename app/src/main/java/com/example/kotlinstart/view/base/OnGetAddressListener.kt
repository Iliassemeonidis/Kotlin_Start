package com.example.kotlinstart.view.base

import com.example.kotlinstart.model.Weather

interface OnGetAddressListener {
    fun onValidData(weather: Weather)
    fun onError()
    fun onInfo()
}