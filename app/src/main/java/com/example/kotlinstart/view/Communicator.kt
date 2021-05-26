package com.example.kotlinstart.view

import androidx.fragment.app.Fragment
import com.example.kotlinstart.view.data.Weather

internal interface Communicator {

    fun openWeatherDetails(weather: Weather)

    fun openNewFragment(fragment: Fragment)
}
