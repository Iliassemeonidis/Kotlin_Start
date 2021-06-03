package com.example.kotlinstart.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinstart.R
import com.example.kotlinstart.view.weatherscreen.WeatherFragment

internal class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.list_container, WeatherFragment())
            .commitAllowingStateLoss()
    }
}
