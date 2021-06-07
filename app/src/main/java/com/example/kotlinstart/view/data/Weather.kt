package com.example.kotlinstart.view.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(var cityName: String, var region: String, var temperature: String = "27°C") :
    Parcelable

