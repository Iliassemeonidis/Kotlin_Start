package com.example.kotlinstart.model

data class WeatherData(
    val city: String = "Moscow",
    var degrees: String = "27°",
    var weatherCondition: String = "Солнечно",
    var textViewFeelsLike: String = "Ощущается как 27°",
    var icon: String = "",
    var cityIcon: String = "",
    val lat: Double = 1.0,
    val lon: Double = 1.0
)









