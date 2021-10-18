package com.example.kotlinstart.model

import com.example.kotlinstart.view.mainscreen.WeatherDetailsData

sealed class AppState {
    data class Success(val weatherDetailsData: WeatherDetailsData) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Info:AppState()
    object Loading : AppState()
}