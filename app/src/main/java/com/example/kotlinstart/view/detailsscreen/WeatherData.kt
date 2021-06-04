package com.example.kotlinstart.view.detailsscreen

data class WeatherData(
    val city: String,
    val degrees: String = "27°",
    val weatherCondition: String = "Солнечно",
    val textViewFeelsLike: String = "Ощущается как 27°"
)
