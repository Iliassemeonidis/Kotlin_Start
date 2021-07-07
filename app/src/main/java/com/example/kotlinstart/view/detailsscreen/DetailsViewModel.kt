package com.example.kotlinstart.view.detailsscreen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinstart.dto.WeatherDTO
import com.example.kotlinstart.model.AppState
import com.example.kotlinstart.repository.detailsrepository.RepositoryDetailsImpl
import com.example.kotlinstart.repository.loader.Loader
import com.example.kotlinstart.repository.loader.displayWeather

internal class DetailsViewModel(
    private val liveDataForObservation: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: RepositoryDetailsImpl = RepositoryDetailsImpl()
) : ViewModel() {

    private lateinit var city: String
    private val onLoaderListener: Loader.OnWeatherListener =
        object : Loader.OnWeatherListener {

            @RequiresApi(Build.VERSION_CODES.N)
            override fun onLoaded(weatherDTO: WeatherDTO) {
                liveDataForObservation.value = AppState.Success(displayWeather(weatherDTO))
            }

            override fun onFailed(error: Throwable) {
                liveDataForObservation.value = AppState.Error(error)
            }
        }

    fun getLiveData() = liveDataForObservation

    @RequiresApi(Build.VERSION_CODES.N)
    fun getWeatherData(city: String) {
        this.city = city
        liveDataForObservation.value = AppState.Loading
        repositoryImpl.getWeatherDataFromLocalStorage(city, onLoaderListener)
    }
}
