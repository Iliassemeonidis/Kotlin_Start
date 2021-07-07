package com.example.kotlinstart.repository.detailsrepository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.kotlinstart.repository.loader.Loader
import com.example.kotlinstart.repository.loader.getWeatherFromServer

class RepositoryDetailsImpl : RepositoryDetails {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getWeatherDataFromLocalStorage(city: String, listener: Loader.OnWeatherListener) =
        getWeatherFromServer(city, listener)
}
