package com.example.kotlinstart.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherParams(
    var city: String = "",
    var degrees: String = "°",
    var weatherCondition: String = "",
    var textViewFeelsLike: String = "",
    var icon: String = "",
    var cityIcon: String = "",
    val lat: Double = 1.0,
    val lon: Double = 1.0
) : Parcelable









