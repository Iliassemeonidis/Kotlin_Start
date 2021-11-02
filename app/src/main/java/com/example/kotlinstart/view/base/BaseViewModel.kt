package com.example.kotlinstart.view.base

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.repository.weatherrepository.RepositoryImpl
import com.example.kotlinstart.view.base.baseinterface.DialogSearchInterface
import com.example.kotlinstart.view.base.baseinterface.OnGetAddressListener
import com.example.kotlinstart.view.base.baseinterface.OnGetWeatherFragmentList
import com.example.kotlinstart.view.base.baseinterface.OnGetWeatherListListener
import com.example.kotlinstart.view.mainscreen.MainFragment

internal class BaseViewModel(
    private val liveDataForObservation: MutableLiveData<WeatherParams> = MutableLiveData(),
    private val liveDataForNewAddress: MutableLiveData<Weather> = MutableLiveData(),
    private val liveDataForDB: MutableLiveData<MutableList<MainFragment>> = MutableLiveData(),
    private val liveDataForDBWeather: MutableLiveData<MutableList<Weather>> = MutableLiveData(),
    private val liveDataAnswer: MutableLiveData<Boolean> = MutableLiveData(),
    private val repositoryImpl: RepositoryImpl = RepositoryImpl(),
) : ViewModel() {

    fun getWeatherParamsFromDataBase() {
        repositoryImpl.getWeatherParamsFromDataBase(object : OnGetWeatherFragmentList {
            override fun onListFragment(list: MutableList<MainFragment>) {
                liveDataForDB.postValue(list)
            }
        })
    }

    fun getAddress(context: Context, address: String, dialog: DialogSearchInterface) {

        repositoryImpl.getAddress(context, address, object : OnGetAddressListener {

            override fun onValidData(weather: Weather) {
                dialog.showDialog(weather.cityName)
            }

            override fun onError(error: Throwable) {
               Throwable(error.message)
            }

            override fun onEmpty() {
               dialog.showToast()
            }
        })
    }

     fun saveWeather(weather: Weather) {
        liveDataForNewAddress.value = weather
    }

    fun subscribeToNewAddress(): LiveData<Weather> {
        return liveDataForNewAddress
    }
    fun saveCityInDataBase(weatherParams: WeatherParams) {
        repositoryImpl.saveCityInDataBase(weatherParams)
    }

    fun subscribeOnWeatherParams(): LiveData<WeatherParams> {
        return liveDataForObservation
    }

    fun subscribeOnWeatherFromDB(): MutableLiveData<MutableList<MainFragment>> {
        return liveDataForDB
    }

    fun subscribeToDB(): MutableLiveData<MutableList<Weather>> {
        return liveDataForDBWeather
    }

    fun getWeatherFromBD() {
        repositoryImpl.getWeatherFromDataBase(object : OnGetWeatherListListener {
            override fun onListReady(list: MutableList<Weather>) {
                liveDataForDBWeather.postValue(list)
            }
        })
    }
}
