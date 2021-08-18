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
        //val handler = Handler()
        Thread {
            //val data = repositoryWeatherHistoryImpl.getWeatherDataFromLocalStorage()
            /*liveDataForObservation.postValue(repositoryWeatherHistoryImpl.getWeatherDataFromLocalStorage(
                object : CallBack() {
                    override fun call(list: List<HistoryEntity>) {
                        liveDataForObservation.value = list
                    }
                }))*/
            liveDataForObservation.postValue(repositoryWeatherHistoryImpl.getWeatherDataFromLocalStorage())
            /*handler.post {
                liveDataForObservation.value = data
            }*/
        }.start()

        //liveDataForObservation.value = repositoryWeatherHistoryImpl.getWeatherDataFromLocalStorage()
    }

    interface CallBack{
        fun call(list: List<HistoryEntity>)
    }
}
