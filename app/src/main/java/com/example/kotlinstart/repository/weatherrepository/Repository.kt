package com.example.kotlinstart.repository.weatherrepository

import android.content.Context
import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.view.base.OnGetAddressListener
import com.example.kotlinstart.view.base.OnGetWeatherListListener
import com.example.kotlinstart.view.mainscreen.DetailsFragment

interface Repository {

    fun getListCityWeatherFromLocalStorage(): ArrayList<Weather>
    fun getAddress(context: Context, address: String, listener: OnGetAddressListener)
    fun saveCityInDataBase(city: WeatherParams)
    fun getWeatherParamsFromDataBase(list:ArrayList<DetailsFragment>)
    fun getWeatherFromDataBase(listener: OnGetWeatherListListener)
    fun deleteAllWeatherParamsFromDataBase()
}
