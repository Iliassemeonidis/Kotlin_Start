package com.example.kotlinstart.view.weatherscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.repository.RepositoryImpl

internal class WeatherViewModel(
    private val liveDataForObservation: MutableLiveData<ArrayList<Weather>> = MutableLiveData(),
    private val repositoryImpl: RepositoryImpl = RepositoryImpl()
) : ViewModel() {

    fun subscribe(): LiveData<ArrayList<Weather>> {
        return liveDataForObservation
    }

    fun getCitiesList() {
        createWeatherData()
    }

    private fun createWeatherData() {
        liveDataForObservation.value = repositoryImpl.getListCityWeatherFromLocalStorage()
    }
}
