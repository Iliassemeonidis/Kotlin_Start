package com.example.kotlinstart.view.weatherlistscreen

import com.example.kotlinstart.model.Weather

interface OnItemDeleteListener {
  fun onItemDelete(result :Boolean, weather: Weather)
}