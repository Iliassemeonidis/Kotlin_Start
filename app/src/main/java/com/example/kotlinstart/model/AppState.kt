package com.example.kotlinstart.model

sealed class AppState {
    data class Success(val weatherParams: WeatherParams/*WeatherDetailsData*/) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}