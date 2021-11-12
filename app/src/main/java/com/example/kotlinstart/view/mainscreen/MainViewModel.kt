package com.example.kotlinstart.view.mainscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.repository.weatherrepository.RepositoryImpl
import com.example.kotlinstart.view.base.baseinterface.OnGetWeatherFragmentList
import com.example.kotlinstart.view.base.baseinterface.OnGetWeatherListListener
import com.example.kotlinstart.view.detailsscreen.DetailsFragment

internal class MainViewModel(
    private val liveDataForObservation: MutableLiveData<WeatherParams> = MutableLiveData(),
    private val liveDataForDB: MutableLiveData<MutableList<DetailsFragment>> = MutableLiveData(),
    private val repositoryImpl: RepositoryImpl = RepositoryImpl(),
) : ViewModel() {

    fun getWeatherParamsFromDataBase() {
        repositoryImpl.getWeatherParamsFromDataBase(object : OnGetWeatherFragmentList {
            override fun onListFragment(list: MutableList<DetailsFragment>) {
                liveDataForDB.postValue(list)
            }
        })
    }

    fun saveCityInDataBase(weatherParams: WeatherParams) {
        repositoryImpl.saveCityInDataBase(weatherParams)
    }

    fun subscribeOnWeatherParams(): LiveData<WeatherParams> {
        return liveDataForObservation
    }

    fun subscribeOnWeatherFromDB(): MutableLiveData<MutableList<DetailsFragment>> {
        return liveDataForDB
    }


}
