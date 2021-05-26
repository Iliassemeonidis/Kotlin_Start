package com.example.kotlinstart.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.kotlinstart.R
import com.example.kotlinstart.view.data.Weather
import com.example.kotlinstart.view.details.DetailsFragment
import com.example.kotlinstart.view.main.WeatherFragment

internal class MainActivity : AppCompatActivity(), Communicator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.list_container, WeatherFragment())
            .commitAllowingStateLoss()
    }

    override fun openWeatherDetails(weather: Weather) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.list_container, DetailsFragment().apply {
                arguments = bundleOf(WeatherFragment.PERSON_KEY to weather)
            })
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    override fun openNewFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.list_container,fragment)
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }
}
