package com.example.kotlinstart.view.detailsscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinstart.repository.RepositoryImpl
import com.example.kotlinstart.view.data.WeatherData

internal class DetailsViewModel(
    private val liveDataForObservation: MutableLiveData<WeatherData> = MutableLiveData(),
    private val repositoryImpl: RepositoryImpl = RepositoryImpl()
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
        liveDataForObservation.value = repositoryImpl.getWeatherDataFromLocalStorage(city)
    }
}
