package com.example.kotlinstart.view.mainscreen

sealed class SearchCityState {
    data class Success(val city: String) : SearchCityState()
    object Empty : SearchCityState()
    data class Error(val error: Throwable) : SearchCityState()
}

