package com.example.kotlinstart.view.detailsscreen

sealed class DetailsFragmentState {
    data class Success(val weatherDetailsData: WeatherDetailsData) : DetailsFragmentState()
    data class Error(val error: Throwable) : DetailsFragmentState()
    object Info: DetailsFragmentState()
    object Loading : DetailsFragmentState()
}

