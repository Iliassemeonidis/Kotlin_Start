package com.example.kotlinstart.view.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.repository.weatherrepository.RepositoryImpl

internal class MainViewModel(
    private val liveDataForObservation: MutableLiveData<WeatherParams> = MutableLiveData(),
    private val repositoryImpl: RepositoryImpl = RepositoryImpl()
) : ViewModel() {

    fun getAddress(context: Context, address: String) {
        repositoryImpl.getAddress(context, address, object : OnGetAddressListener {

            override fun onValidData(weatherParams: WeatherParams) {
                liveDataForObservation.value = weatherParams
            }

            override fun onError() {
                //AppState
                liveDataForObservation.value = WeatherParams()
            }
        })
    }

    fun subscribeOnWeatherParams(): LiveData<WeatherParams> {
        return liveDataForObservation
    }
}
