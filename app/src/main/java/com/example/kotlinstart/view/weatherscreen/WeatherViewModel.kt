package com.example.kotlinstart.view.weatherscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.repository.weatherrepository.RepositoryImpl
import com.example.kotlinstart.view.base.baseinterface.OnGetWeatherListListener

// пока не удалил ее так как она возможно она понадобится для запросов в бд
internal class WeatherViewModel(
    private val liveDataForNewAddress: MutableLiveData<Weather> = MutableLiveData(),
    private val liveDataForDB: MutableLiveData<MutableList<Weather>> = MutableLiveData(),
    private val repositoryImpl: RepositoryImpl = RepositoryImpl(),
) : ViewModel() {


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
