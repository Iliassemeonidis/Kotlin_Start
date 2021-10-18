package com.example.kotlinstart.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    var cityName: String = "",
    var region: String = "",
    var temperature: String = ""
) : Parcelable

//todo take cities from BD
fun getCityWeather() = arrayListOf(
    Weather("Москва", "Москва"),
    Weather("Лос-Анджелес", "Калифорния"),
    Weather("Афины", "Аттика"),
    Weather("Сочи", "Краснодарский край"),
    Weather("Владикавказ", "Северная Осетия"),
    Weather("Таймырский", "Долгано-Ненецкий")
)
