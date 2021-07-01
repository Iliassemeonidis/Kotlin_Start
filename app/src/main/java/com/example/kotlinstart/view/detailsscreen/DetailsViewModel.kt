package com.example.kotlinstart.view.detailsscreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinstart.loading.Loader
import com.example.kotlinstart.model.WeatherData
import com.example.kotlinstart.repository.detailsrepository.RepositoryImplDetails

internal class DetailsViewModel(
    private val liveDataForObservation: MutableLiveData<WeatherData> = MutableLiveData(),
    private val repositoryImpl: RepositoryImplDetails = RepositoryImplDetails()
) : ViewModel() {

    lateinit var city: String

    fun subscribe(): LiveData<WeatherData> {
        return liveDataForObservation
    }

    fun setCityData(city: String) {
        this.city = city
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun getWeatherDataByCityName() {
        createWeatherData()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun createWeatherData() {
        liveDataForObservation.value = repositoryImpl.getWeatherDataFromLocalStorage(city)
        Loader(repositoryImpl, liveDataForObservation, city).loadWeather()
    }
}
