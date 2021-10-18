package com.example.kotlinstart.view.weatherscreen

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.repository.weatherrepository.RepositoryImpl
import com.example.kotlinstart.view.base.OnGetAddressListener
import com.example.kotlinstart.view.base.OnGetWeatherListListener

internal class WeatherViewModel(
    private val liveDataForNewAddress: MutableLiveData<Weather> = MutableLiveData(),
    private val liveDataForDB: MutableLiveData<MutableList<Weather>> = MutableLiveData(),
    private val repositoryImpl: RepositoryImpl = RepositoryImpl(),
) : ViewModel() {

    fun getAddress(context: Context, address: String) {
        repositoryImpl.getAddress(context, address, object : OnGetAddressListener {
            override fun onValidData(weather: Weather) {
                liveDataForNewAddress.value = weather

            }

            override fun onError() {
                //TODO Сделать через AppState
                liveDataForNewAddress.value = Weather()
            }

            override fun onInfo() {
                //TODO Сделать через AppState
                liveDataForNewAddress.value = Weather()
            }
        })
    }

    fun subscribeToNewAddress(): LiveData<Weather> {
        return liveDataForNewAddress
    }


    fun getWeatherFromBD() {
        repositoryImpl.getWeatherFromDataBase(object : OnGetWeatherListListener {
            override fun onListReady(list: MutableList<Weather>) {
                liveDataForDB.postValue(list)
            }
        })
    }

    fun saveCityInDataBase(weatherParams: WeatherParams) {
        repositoryImpl.saveCityInDataBase(weatherParams)
    }

    fun deleteAllWeatherData() {
        repositoryImpl.deleteAllWeatherParamsFromDataBase()
    }

    fun subscribeToDB(): MutableLiveData<MutableList<Weather>> {
        return liveDataForDB
    }
}
