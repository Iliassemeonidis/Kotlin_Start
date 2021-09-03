package com.example.kotlinstart.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherParams(
    var city: String = "Moscow",
    var degrees: String = "27°",
    var weatherCondition: String = "Солнечно",
    var textViewFeelsLike: String = "Ощущается как 27°",
    var icon: String = "",
    var cityIcon: String = "",
    val lat: Double = 1.0,
    val lon: Double = 1.0
) : Parcelable









