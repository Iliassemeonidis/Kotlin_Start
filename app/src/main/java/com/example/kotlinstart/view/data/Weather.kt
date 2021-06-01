package com.example.kotlinstart.view.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class Weather(var cityName: String, var region: String, var temperature: String) :
    Parcelable

