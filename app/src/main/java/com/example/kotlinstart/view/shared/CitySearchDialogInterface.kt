package com.example.kotlinstart.view.shared

import com.example.kotlinstart.model.WeatherParams

interface CitySearchDialogInterface {
    fun showCitySelectionDialog(city: WeatherParams)
}