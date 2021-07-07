package com.example.kotlinstart.repository.detailsrepository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.kotlinstart.model.getWeatherFromServer

class RepositoryDetailsImpl : RepositoryDetails {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getWeatherDataFromLocalStorage(city:String) = getWeatherFromServer(city)
}