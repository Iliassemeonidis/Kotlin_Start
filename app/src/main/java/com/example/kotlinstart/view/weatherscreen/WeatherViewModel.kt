package com.example.kotlinstart.view.weatherscreen

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.repository.geolocationrepository.RepositoryGeolocationHelperImpl
import com.example.kotlinstart.repository.weatherrepository.RepositoryImpl
import com.example.kotlinstart.view.main.OnGetAddressListener

internal class WeatherViewModel(
    private val liveDataForObservation: MutableLiveData<ArrayList<Weather>> = MutableLiveData(),
    private val repositoryImpl: RepositoryImpl = RepositoryImpl(),
) : ViewModel() {

    fun getAddress(context: Context, address: String) {
        repositoryImpl.getAddress(context, address, object : OnGetAddressListener {
            override fun onValidData(weather: Weather) {
                liveDataForObservation.value = arrayListOf(weather)
            }

            override fun onError() {
                TODO("Not yet implemented")
            }

            override fun onInfo() {
                TODO("Not yet implemented")
            }


        })
    }

    fun subscribe(): LiveData<ArrayList<Weather>> {
        return liveDataForObservation
    }


    fun createWeatherData(list: ArrayList<Weather>) {
        repositoryImpl.getWeatherFromDataBase(list)
        liveDataForObservation.postValue(list)
    }

    fun saveCityInDataBase(weatherParams: WeatherParams) {
        repositoryImpl.saveCityInDataBase(weatherParams)
    }

    fun deleteAllWeatherData() {
        repositoryImpl.deleteAllWeatherParamsFromDataBase()
    }

}
