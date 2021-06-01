package com.example.kotlinstart.view.detailsscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinstart.view.data.Weather

internal class DetailsViewModel(
    private val liveDataForObservation: MutableLiveData<ArrayList<Weather>> = MutableLiveData()
) : ViewModel() {

    fun subscribe(): LiveData<ArrayList<Weather>> {
        return liveDataForObservation
    }

    fun getCitiesList() {
        createWeatherData()
    }

    private fun createWeatherData() {
        // на сколько понимаю тут должен идти запрос в бд от куда получаем список
        // тут мы его обрабатываем и по подписке передаем во фрагмент

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
