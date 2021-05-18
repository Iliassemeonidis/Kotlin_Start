package com.example.kotlinstart

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
internal data class Person(var name: String?, var age: Int) : Parcelable
