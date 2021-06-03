package com.example.kotlinstart.view.detailsscreen

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

internal class DetailsViewModel(
    private val liveDataForObservation: MutableLiveData<String> = MutableLiveData(),
    val application: Application,
    var city: String
) : ViewModel() {

    fun subscribe(): LiveData<String> {
        return liveDataForObservation
    }

    fun getCityName() {
        createWeatherData()
    }

    private fun createWeatherData() {
        liveDataForObservation.value = city
    }
}
