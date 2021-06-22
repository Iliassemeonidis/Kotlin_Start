package com.example.kotlinstart.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    var cityName: String = "Москва",
    var region: String = "Москва",
    var temperature: String = "27°C"
) : Parcelable

fun getCityWeather() = arrayListOf(
    Weather("Москва", "Москва"),
    Weather("Лос-Анджелес", "Калифорния"),
    Weather("Афины", "Аттика"),
    Weather("Сочи", "Краснодарский край"),
    Weather("Владикавказ", "Республика Северная Осетия-Алания"),
    Weather("Мурманск", "Мурманская область"),
    Weather("Таймырский", "Долгано-Ненецкий")
)
