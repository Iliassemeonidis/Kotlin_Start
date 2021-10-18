package com.example.kotlinstart.view.base

import com.example.kotlinstart.model.Weather

interface OnGetWeatherListListener {
    fun onListReady(list: MutableList<Weather>)
}