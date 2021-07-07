package com.example.kotlinstart.model

sealed class AppState {
    data class Success(val weatherData: WeatherData) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}