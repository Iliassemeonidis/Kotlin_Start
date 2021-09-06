package com.example.kotlinstart.repository.geolocationrepository

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.example.kotlinstart.model.WeatherParams

interface RepositoryGeolocation {
//    fun getMyGeolocation(context: Context,fragment: Fragment,activity: Activity):MyGeolocationHelper
    fun gatWeatherCoordinatesByCityName():WeatherParams
}