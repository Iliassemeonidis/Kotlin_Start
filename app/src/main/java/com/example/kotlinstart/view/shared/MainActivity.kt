package com.example.kotlinstart.view.shared

import android.Manifest
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.example.kotlinstart.KotlinStartApplication.Companion.getGeolocationHelper
import com.example.kotlinstart.R
import com.example.kotlinstart.databinding.ActivityMainBinding
import com.example.kotlinstart.location.PermissionInterface
import com.example.kotlinstart.location.REQUEST_CODE
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.view.detailsscreen.DetailsFragment
import com.example.kotlinstart.view.detailsscreen.DetailsViewPagerAdapter
import com.example.kotlinstart.view.weatherscreen.WeatherFragment
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

internal class MainActivity : AppCompatActivity(),PermissionInterface {
    private var isMain = true
    private lateinit var mainBinding: ActivityMainBinding
    private val binding get() = mainBinding
    private val geolocationHelper = getGeolocationHelper()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBindingAndAdapter()
        createBottomBar()
        initNavigationIcon()
        checkPermissions()
    }


    private fun initBindingAndAdapter() {
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        initPagerAdapter()
    }

    private fun initPagerAdapter() {
        val adapter = DetailsViewPagerAdapter(
            this,
            arrayListOf(
                DetailsFragment.newInstance(WeatherParams("Пенза")),
                DetailsFragment(),
                DetailsFragment.newInstance(WeatherParams("Уфа"))
            )
        )
        pager.adapter = adapter
    }

    private fun initNavigationIcon() {
        binding.bottomAppBar.setNavigationOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.list_container, WeatherFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
            setVisibilityInFAB(false)
        }
    }

    private fun createBottomBar() {
        setSupportActionBar(binding.bottomAppBar)
        binding.fab.setOnClickListener {
            if (isMain) {
                isMain = false

                binding.bottomAppBar.navigationIcon = null
                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END

                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_back
                    )
                )
                binding.bottomAppBar.replaceMenu(R.menu.search_menu)
                searchCity()
            } else {
                isMain = true
                binding.bottomAppBar.navigationIcon =
                    ContextCompat.getDrawable(this, R.drawable.ic_hamburger_menu_bottom_bar)

                binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER

                binding.fab.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_plus_cross
                    )
                )
                binding.bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
            }
        }
    }


    private fun searchCity() {
        val search = binding.bottomAppBar.menu.getItem(0).actionView as SearchView

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!=null) {
                    if (newText.length > 1) {
                        geolocationHelper.getAddressAsyncByCity(this@MainActivity, newText)
                    }
                }
                return true
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav -> Toast.makeText(this, R.string.favourite, Toast.LENGTH_SHORT).show()
            R.id.app_bar_settings -> Toast.makeText(this, R.string.settings, Toast.LENGTH_SHORT)
                .show()
        }
        return super.onOptionsItemSelected(item)
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
                setVisibilityInFAB(true)
                isExit = false
            }
        }
    }

    private fun checkExit() {
        when (isExit) {
            true -> {
                super.onBackPressed()
                setVisibilityInFAB(true)
            }
            else -> {
                Toast.makeText(this, R.string.exit, Toast.LENGTH_SHORT).show()
                isExit = true
            }
        }
    }

    companion object {
        var isExit = false
    }


    fun setVisibilityInFAB(visibility: Boolean) {
        if (visibility) {
            binding.fab.visibility = View.VISIBLE
        } else {
            binding.fab.visibility = View.INVISIBLE
        }
    }

    private fun checkPermissions() {
        val parentLayout = findViewById<View>(android.R.id.content)
        Snackbar.make(
            parentLayout,
            "Определить погоду по Вашей геолокации?",
            6000
        ).setAction("Определить") {
            geolocationHelper.checkPermission(this, this)
        }
            .show()
    }

    override fun requestPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE
        )
    }

}
