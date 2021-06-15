package com.example.kotlinstart.view.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CityData (val cityName:String): Parcelable

fun getListOfCityNames() = arrayListOf(
    CityData("Абу-Даби"),
    CityData("Абердинг"),
    CityData("Аракажу"),
    CityData("Арнем"),
    CityData("Аврора"),
    CityData("Авила"),
    CityData("Авиньон")
)

