package com.example.kotlinstart.view.detailsscreen

import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinstart.model.AppState
import com.example.kotlinstart.model.WeatherData
import com.example.kotlinstart.repository.detailsrepository.RepositoryDetailsImpl
import java.lang.Thread.sleep

internal class DetailsViewModel(
    private val liveDataForObservation: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: RepositoryDetailsImpl = RepositoryDetailsImpl()
) : ViewModel() {

    lateinit var city: String

    fun getLiveData() = liveDataForObservation

    fun setCityData(city: String) {
        this.city = city
    }

    @RequiresApi(Build.VERSION_CODES.N)
     fun getWeatherData() {
        liveDataForObservation.value = AppState.Loading
        Thread {
            sleep(1000)
            liveDataForObservation.postValue(AppState.Success(repositoryImpl.getWeatherDataFromLocalStorage(city)))
        }.start()
    }
}
