package com.example.kotlinstart.view.shared

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinstart.R
import com.example.kotlinstart.view.detailsscreen.DetailsFragment
import com.example.kotlinstart.view.weatherscreen.WeatherFragment
import java.io.BufferedReader
import java.util.stream.Collectors

/*
по ДЗ:
-прочитать 4 методичку
-перенести запросы в Репозиторий
-прочитать 5 методичку и сделать свой браузер в Погодном приложении
* */

internal class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.list_container, DetailsFragment())
            .commitAllowingStateLoss()
    }

    override fun onBackPressed() {
        when (supportFragmentManager.backStackEntryCount) {
            0 -> checkExit()
            else -> {
                supportFragmentManager.popBackStack()
                isExit = false
            }
        }
    }

    private fun checkExit() {
        when (isExit) {
            true -> super.onBackPressed()
            else -> {
                Toast.makeText(this, R.string.exit, Toast.LENGTH_SHORT).show()
                isExit = true
            }
        }
    }

    companion object {
        var isExit = false
    }

}
