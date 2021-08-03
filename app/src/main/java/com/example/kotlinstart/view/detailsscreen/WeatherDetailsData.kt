package com.example.kotlinstart.view.detailsscreen

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherDetailsData(
    val city: String,
    val degrees: String,
    val condition: String,
    val feelsLike: String,
    var icon: String,
    var cityIconURL: String
):Parcelable