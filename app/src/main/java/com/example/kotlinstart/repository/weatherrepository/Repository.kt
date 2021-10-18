package com.example.kotlinstart.repository.weatherrepository

import android.content.Context
import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.room.HistoryEntity
import com.example.kotlinstart.view.detailsscreen.DetailsFragment
import com.example.kotlinstart.view.main.OnGetAddressListener

interface Repository {

    fun getListCityWeatherFromLocalStorage(): ArrayList<Weather>
    fun getAddress(context: Context, address: String, listener: OnGetAddressListener)
    fun saveCityInDataBase(city: WeatherParams)
    fun getWeatherParamsFromDataBase(list:ArrayList<DetailsFragment>)
    fun getWeatherFromDataBase(list:ArrayList<Weather>)
    fun deleteAllWeatherParamsFromDataBase()
}
