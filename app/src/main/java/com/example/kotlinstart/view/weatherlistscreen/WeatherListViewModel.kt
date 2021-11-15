package com.example.kotlinstart.view.weatherlistscreen

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.repository.weatherrepository.RepositoryImpl
import com.example.kotlinstart.view.base.baseinterface.OnGetAddressListener
import com.example.kotlinstart.view.base.baseinterface.OnGetWeatherListListener
import com.example.kotlinstart.view.detailsscreen.SearchCityState

internal class WeatherListViewModel(
    private val liveDataForNewAddress: MutableLiveData<Weather> = MutableLiveData(),
    private val liveDataForDB: MutableLiveData<MutableList<Weather>> = MutableLiveData(),
    private val repositoryImpl: RepositoryImpl = RepositoryImpl(),
    private val liveDataForNewCity: MutableLiveData<SearchCityState> = MutableLiveData()
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

    fun getAddress(context: Context, address: String) {
        repositoryImpl.getAddress(context, address, object : OnGetAddressListener {

            override fun onValidData(weather: Weather) {
                liveDataForNewCity.value = SearchCityState.Success(weather.cityName)
            }

            override fun onError(error: Throwable) {
                liveDataForNewCity.value = SearchCityState.Error(error)
            }

            override fun onEmpty() {
                liveDataForNewCity.value = SearchCityState.Empty
            }
        })
    }

    fun onCityApprovedByUser(weather: Weather) {
        //liveDataForNewAddress.value = weather
        repositoryImpl.saveCityInDataBase(WeatherParams().apply {
            city = weather.cityName
        })
    }

    fun subscribeToNewCity(): LiveData<SearchCityState> {
        return liveDataForNewCity
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
