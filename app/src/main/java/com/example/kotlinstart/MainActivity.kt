package com.example.kotlinstart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf

internal class MainActivity : AppCompatActivity(), Communicator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.list_container, ListOfCitiesFragment())
            .commitAllowingStateLoss()
    }

    override fun passDataComm(weather: Weather) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.list_container, DetailsFragment().apply {
                arguments = bundleOf(ListOfCitiesFragment.PERSON_KEY to weather)
            })
            .addToBackStack(null).commitAllowingStateLoss()
    }
}
