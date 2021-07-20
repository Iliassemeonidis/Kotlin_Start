package com.example.kotlinstart.model

sealed class LocationConstant {
    data class Moscow(val lat: Double = 55.755826, val lon: Double = 37.617299900000035) : LocationConstant()
    data class Athens(val lat: Double = 37.9794500, val lon: Double = 23.7162200) : LocationConstant()
}

