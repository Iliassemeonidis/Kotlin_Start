package com.example.kotlinstart.view.main

import com.example.kotlinstart.model.Weather

interface OnGetAddressListener {
    fun onValidData(weather: Weather)
    fun onError()
    fun onInfo()
}