package com.example.kotlinstart.view.weatherlistscreen

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

internal sealed class ListState : Parcelable {

    @Parcelize
    data class NotChanged(val refresh: Boolean) : ListState()
    @Parcelize
    data class ToPosition(val position: Int, val refresh: Boolean) : ListState()
}
