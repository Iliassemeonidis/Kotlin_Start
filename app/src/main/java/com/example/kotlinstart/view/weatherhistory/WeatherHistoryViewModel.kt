package com.example.kotlinstart.view.weatherhistory

import android.os.Handler
import android.os.HandlerThread
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
    val handlerThread = HandlerThread("MyThread")
    handlerThread.start()
    val handler = Handler(handlerThread.looper)
        handler.post{
            liveDataForObservation.postValue(repositoryWeatherHistoryImpl.getWeatherDataFromLocalStorage())
        }
        Thread {
            //val data = repositoryWeatherHistoryImpl.getWeatherDataFromLocalStorage()
            /*liveDataForObservation.postValue(repositoryWeatherHistoryImpl.getWeatherDataFromLocalStorage(
                object : CallBack() {
                    override fun call(list: List<HistoryEntity>) {
                        liveDataForObservation.value = list
                    }
                      handler.post {
                liveDataForObservation.value = data
            }*/


        /* handler.post{
                liveDataForObservation.value = repositoryWeatherHistoryImpl.getWeatherDataFromLocalStorage()
            }*/


//            liveDataForObservation.postValue(repositoryWeatherHistoryImpl.getWeatherDataFromLocalStorage())

        }.start()


    }

    interface CallBack{
        fun call(list: List<HistoryEntity>)
    }
}
