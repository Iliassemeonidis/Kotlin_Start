package com.example.kotlinstart.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.repository.weatherrepository.RepositoryImpl
import com.example.kotlinstart.view.detailsscreen.DetailsFragment

internal class MainViewModel(
    private val liveDataForObservation: MutableLiveData<WeatherParams> = MutableLiveData(),
    private val repositoryImpl: RepositoryImpl = RepositoryImpl()
) : ViewModel() {

    fun saveCityInDataBase(weatherParams: WeatherParams) {
        repositoryImpl.saveCityInDataBase(weatherParams)
    }

    fun getWeatherParamsFromDataBase(list: ArrayList<DetailsFragment>) {
        repositoryImpl.getWeatherParamsFromDataBase(list)
    }

    fun subscribeOnWeatherParams(): LiveData<WeatherParams> {
        return liveDataForObservation
    }
}
