package com.example.kotlinstart.view.shared

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.FragmentActivity
import com.example.kotlinstart.R
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.view.detailsscreen.DetailsFragment
import com.example.kotlinstart.view.detailsscreen.DetailsViewPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

internal class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.list_container, DetailsFragment())
//            .commitAllowingStateLoss()

        //  todo прееделать, добавлять новые города через фб
        val adapter = DetailsViewPagerAdapter(this,arrayListOf(DetailsFragment.newInstance(WeatherParams("Пенза")), DetailsFragment(), DetailsFragment.newInstance(WeatherParams("Уфа"))))
        pager.adapter = adapter
        pager.setCurrentItem(1,true)
    }



    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        when (newConfig.uiMode) {
            Configuration.UI_MODE_NIGHT_NO -> {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_NO
                )
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                AppCompatDelegate.setDefaultNightMode(
                    AppCompatDelegate.MODE_NIGHT_YES
                )
            }
        }
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
