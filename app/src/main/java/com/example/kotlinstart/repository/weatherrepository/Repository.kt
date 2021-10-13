package com.example.kotlinstart.repository.weatherrepository

import android.content.Context
import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.view.main.OnGetAddressListener

interface Repository {

    fun getListCityWeatherFromLocalStorage(): ArrayList<Weather>
    fun getAddress(context: Context, address: String, listener: OnGetAddressListener)
}
