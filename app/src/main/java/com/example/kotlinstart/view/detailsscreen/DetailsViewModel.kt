package com.example.kotlinstart.view.detailsscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

internal class DetailsViewModel(
    private val liveDataForObservation: MutableLiveData<WeatherData> = MutableLiveData(),
) : ViewModel() {

    lateinit var city: String

    fun subscribe(): LiveData<WeatherData> {
        return liveDataForObservation
    }

    fun setCityData(city: String) {
        this.city = city
    }

    fun getWeatherDataByCityName() {
        createWeatherData()
    }

    private fun createWeatherData() {
        liveDataForObservation.value = WeatherData(city)
    }
}
