package com.example.kotlinstart.view.detailsscreen

data class WeatherDetailsData(
    val city: String,
    val degrees: String,
    val condition: String,
    val feelsLike: String,
    var icon: String,
    var cityIconURL: String
)