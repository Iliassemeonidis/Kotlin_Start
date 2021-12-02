package com.example.kotlinstart.repository.weatherrepository

import android.content.Context
import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.view.base.baseinterface.OnGetAddressListener
import com.example.kotlinstart.view.base.baseinterface.OnGetWeatherFragmentList
import com.example.kotlinstart.view.base.baseinterface.OnGetWeatherListListener

interface Repository {
    fun getAddress(context: Context, address: String, listener: OnGetAddressListener)
    fun saveCityInDataBase(city: WeatherParams)
    fun getWeatherParamsFromDataBase(listener: OnGetWeatherFragmentList)
    fun getWeatherFromDataBase(listener: OnGetWeatherListListener)
    fun deleteAllWeatherParamsFromDataBase(weather: Weather)
}
