package com.example.kotlinstart.view.weatherscreen

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.repository.geolocationrepository.RepositoryGeolocationHelperImpl
import com.example.kotlinstart.repository.weatherrepository.RepositoryWeatherImpl
import com.example.kotlinstart.view.location.MyGeolocationHelper

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
