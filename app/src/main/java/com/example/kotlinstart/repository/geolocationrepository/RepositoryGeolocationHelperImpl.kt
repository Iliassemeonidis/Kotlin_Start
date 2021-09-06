package com.example.kotlinstart.repository.geolocationrepository

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import com.example.kotlinstart.model.WeatherParams

internal class RepositoryGeolocationHelperImpl : RepositoryGeolocation {
   /* override fun getMyGeolocation(
        context: Context,
        fragment: Fragment,
        activity: Activity
    ): MyGeolocationHelper {
        return MyGeolocationHelper(context, fragment, activity)
    }*/

    override fun gatWeatherCoordinatesByCityName(): WeatherParams {
        TODO("Not yet implemented")
    }
}