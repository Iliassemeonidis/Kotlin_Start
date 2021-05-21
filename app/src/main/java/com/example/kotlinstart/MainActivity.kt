package com.example.kotlinstart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import com.example.kotlinstart.weather.Weather
import com.example.kotlinstart.weather.WeatherFragment

internal class MainActivity : AppCompatActivity(), Communicator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.list_container, WeatherFragment())
            .commitAllowingStateLoss()
    }

    override fun passDataComm(weather: Weather) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.list_container, DetailsFragment().apply {
                arguments = bundleOf(WeatherFragment.PERSON_KEY to weather)
            })
            .addToBackStack(null).commitAllowingStateLoss()
    }
}
