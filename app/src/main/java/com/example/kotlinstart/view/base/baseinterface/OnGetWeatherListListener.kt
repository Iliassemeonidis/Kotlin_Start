package com.example.kotlinstart.view.base.baseinterface

import com.example.kotlinstart.model.Weather

interface OnGetWeatherListListener {
    fun onListReady(list: MutableList<Weather>)
}