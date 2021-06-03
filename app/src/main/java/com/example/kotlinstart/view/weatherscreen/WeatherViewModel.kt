package com.example.kotlinstart.view.weatherscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinstart.view.data.Weather

internal class WeatherViewModel(
    private val liveDataForObservation: MutableLiveData<ArrayList<Weather>> = MutableLiveData()
) : ViewModel() {

    fun subscribe(): LiveData<ArrayList<Weather>> {
        return liveDataForObservation
    }

    fun getCitiesList() {
        createWeatherData()
    }

    private fun createWeatherData() {
        val cityArray = listOf("Москва", "Калифорния", "Афины", "Сочи", "Владикавказ")
        val regionArray = listOf(
            "Москва",
            "Мериленд",
            "Аттика",
            "Краснодарский край",
            "Республика Северная Осетия-Алания "
        )
        val weather = ArrayList<Weather>()
        if (cityArray.size == regionArray.size) {
            for (i in cityArray.indices) {
                weather.add(Weather(cityArray[i], regionArray[i], "27°C"))
            }
        }
        liveDataForObservation.value = weather
    }
}
