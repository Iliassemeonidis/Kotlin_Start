package com.example.kotlinstart.view.weatherscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.repository.geolocationrepository.RepositoryGeolocationHelperImpl
import com.example.kotlinstart.repository.weatherrepository.RepositoryWeatherImpl

internal class WeatherViewModel(
    private val liveDataForObservation: MutableLiveData<ArrayList<Weather>> = MutableLiveData(),
    private val repositoryImpl: RepositoryWeatherImpl = RepositoryWeatherImpl(),
    private val repositoryGeolocationHelperImpl: RepositoryGeolocationHelperImpl = RepositoryGeolocationHelperImpl()
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
