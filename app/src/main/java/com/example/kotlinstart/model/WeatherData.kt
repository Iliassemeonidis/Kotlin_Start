package com.example.kotlinstart.model

data class WeatherData(
    val city: String,
    val degrees: String = "27°",
    val weatherCondition: String = "Солнечно",
    val textViewFeelsLike: String = "Ощущается как 27°"
)

fun getDetailWeather(city: String ="Москва") = when (city) {
    "Москва" -> WeatherData(city, "27°", "Солнечно", "Ощущается как 30°")
    "Калифорния", "Афины", "Сочи" -> WeatherData(city)
    "Владикавказ" -> WeatherData(city, "27°", "Солнечно", "Ощущается как 30°")
    else -> WeatherData(city)
}




