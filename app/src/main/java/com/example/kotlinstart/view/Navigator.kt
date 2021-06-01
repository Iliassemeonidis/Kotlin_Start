package com.example.kotlinstart.view

import androidx.fragment.app.Fragment
import com.example.kotlinstart.view.data.Weather

internal interface Navigator {

    fun openWeatherDetails(weather: Weather)

    fun openNewFragment(fragment: Fragment)
}
