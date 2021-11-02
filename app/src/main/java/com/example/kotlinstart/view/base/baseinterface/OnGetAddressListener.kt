package com.example.kotlinstart.view.base.baseinterface

import com.example.kotlinstart.model.Weather

interface OnGetAddressListener {
    fun onValidData(weather: Weather)
    fun onError(error: Throwable)
    fun onEmpty()
}