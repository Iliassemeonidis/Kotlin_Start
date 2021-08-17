package com.example.kotlinstart.view.weatherhistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinstart.repository.detailsrepository.datasource.LocalDataSource
import com.example.kotlinstart.repository.weatherhistoryrepository.RepositoryWeatherHistoryImpl
import com.example.kotlinstart.room.HistoryEntity

class WeatherHistoryViewModel(
    private val liveDataForObservation: MutableLiveData<List<HistoryEntity>> = MutableLiveData(),
    private val repositoryWeatherHistoryImpl: RepositoryWeatherHistoryImpl = RepositoryWeatherHistoryImpl(
        LocalDataSource()
    )
) : ViewModel() {

    fun subscribe(): LiveData<List<HistoryEntity>> {
        return liveDataForObservation
    }

    fun getCitiesList() {
        createWeatherData()
    }

    private fun createWeatherData() {
        liveDataForObservation.value = repositoryWeatherHistoryImpl.getWeatherDataFromLocalStorage()
    }
}
