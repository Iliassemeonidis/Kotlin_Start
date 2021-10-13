package com.example.kotlinstart.repository.weatherrepository

import android.content.Context
import com.example.kotlinstart.KotlinStartApplication
import com.example.kotlinstart.model.getCityWeather
import com.example.kotlinstart.view.main.OnGetAddressListener

class RepositoryImpl : Repository {

    override fun getListCityWeatherFromLocalStorage() = getCityWeather()

    override fun getAddress(context: Context, address: String, listener: OnGetAddressListener) {
        KotlinStartApplication.getGeolocationHelper().getAddressAsyncByCity(
            context,
            address,
            listener
        )
    }
}
