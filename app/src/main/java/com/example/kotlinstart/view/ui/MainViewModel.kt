package com.example.kotlinstart.view.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinstart.view.data.Weather

internal class MainViewModel(
    private val liveDataToObserver: MutableLiveData<Any> = MutableLiveData()
) : ViewModel() {

    fun getData(): LiveData<Any> {
        gelListData()
        return liveDataToObserver
    }

    private fun gelListData() {
        // на сколько понимаю тут должен идти запрос в бд от куда получаем список
        // тут мы его обрабатываем и по подписке передаем в

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
        liveDataToObserver.value = weather
    }
}