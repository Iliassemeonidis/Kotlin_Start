package com.example.kotlinstart.model

fun getDetailWeather(city: String = "Москва") = when (city) {
    "Москва" -> WeatherParams(
        city,
        "27°",
        "Солнечно",
        "Ощущается как 30°",
        lat = 55.755826,
        lon = 37.617299900000035
    )
    "Афины" -> WeatherParams(
        city,
        "27°",
        "Солнечно",
        "Ощущается как 30°",
        lat = 37.9794500,
        lon = 23.7162200
    )
    "Сочи" -> WeatherParams(
        city,
        "27°",
        "Солнечно",
        "Ощущается как 30°",
        lat = 43.5991700,
        lon = 39.7256900
    )
    "Лос-Анджелес" -> WeatherParams(
        city,
        "27°",
        "Солнечно",
        "Ощущается как 30°",
        lat = 34.0522300,
        lon = -118.2436800
    )
    "Владикавказ" -> WeatherParams(
        city,
        "27°",
        "Солнечно",
        "Ощущается как 30°",
        lat = 43.0366700,
        lon = 44.6677800
    )
    "Мурманск" -> WeatherParams(
        city,
        "27°",
        "Солнечно",
        "Ощущается как 30°",
        lat = 68.9791700,
        lon = 33.0925100
    )
    "Таймырский" -> WeatherParams(
        city,
        "7°",
        "Холодновато",
        "Ощущается как",
        lat = 80.610813,
        lon = 94.485730
    )
    else -> WeatherParams(city)
}
