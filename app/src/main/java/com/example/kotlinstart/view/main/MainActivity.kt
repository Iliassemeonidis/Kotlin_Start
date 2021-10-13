package com.example.kotlinstart.view.main

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinstart.KotlinStartApplication.Companion.getHistoryDao
import com.example.kotlinstart.R
import com.example.kotlinstart.databinding.ActivityMainBinding
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.room.HistoryEntity
import com.example.kotlinstart.view.detailsscreen.DetailsFragment
import com.example.kotlinstart.view.detailsscreen.DetailsViewPagerAdapter
import com.example.kotlinstart.view.weatherscreen.WeatherFragment
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.activity_main.*


//По ДЗ:
//- Genymotion https://www.genymotion.com/download/
//- Переход с Main на Weather через FAB. Возврат обратно по кнопке меню справа.
//- Реализовать сохранение данных через VM
//- Использовать AppState в качестве Unidirectional data flow
//- Формировать ViewPager из БД

internal class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var mainBinding: ActivityMainBinding
    private val binding get() = mainBinding
    private var isMain = true
    private var isExit = false

    private val listWeatherParams: ArrayList<DetailsFragment> = ArrayList()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.subscribeOnWeatherParams().observe(this@MainActivity, { renderData(it) })
        initBindingAndAdapter()
        createBottomBar()
        initNavigationIcon()
    }


    private fun initBindingAndAdapter() {
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        initPagerAdapter()
    }

    private fun initPagerAdapter() {
        //todo take data from BD if is empty show dialog
        listWeatherParams.add(DetailsFragment.newInstance(WeatherParams("Пенза")))
        listWeatherParams.add(DetailsFragment.newInstance(WeatherParams("Уфа")))
        listWeatherParams.add(DetailsFragment())
        val adapter = DetailsViewPagerAdapter(
            this,
            listWeatherParams
        )
        pager.adapter = adapter
    }

    private fun initNavigationIcon() {
        binding.bottomAppBar.setNavigationOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.list_container, WeatherFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
            setVisibilityInFab(false)
        }
    }

    private fun createBottomBar() {
        setSupportActionBar(binding.bottomAppBar)
        binding.fab.setOnClickListener {
            if (isMain) {
                isMain = false
                changeFabIcon(isMain)
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

    private fun changeFabIcon(isMain: Boolean) {
        binding.bottomAppBar.navigationIcon = null
        binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END

        binding.fab.setImageDrawable(
            ContextCompat.getDrawable(
                this,
                R.drawable.ic_back
            )
        )
        binding.bottomAppBar.replaceMenu(R.menu.search_menu)
    }

    private fun searchCity() {
        val search = binding.bottomAppBar.menu.getItem(0).actionView as SearchView

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    if (newText.length > 4) {
                        viewModel.getAddress(this@MainActivity, newText)
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
                setVisibilityInFab(true)
                isExit = false
            }
        }
    }

    private fun checkExit() {
        when (isExit) {
            true -> {
                super.onBackPressed()
                setVisibilityInFab(true)
            }
            else -> {
                Toast.makeText(this, R.string.exit, Toast.LENGTH_SHORT).show()
                isExit = true
            }
        }
    }

    private fun setVisibilityInFab(visibility: Boolean) {
        if (visibility) {
            binding.fab.visibility = View.VISIBLE
        } else {
            binding.fab.visibility = View.INVISIBLE
        }
    }

    private fun renderData(weatherParams: WeatherParams) {
        if (weatherParams.city.isBlank()) {
            Toast.makeText(this, "Список адресов пустой", Toast.LENGTH_SHORT).show()
        } else {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.botton_search))
                .setMessage(getString(R.string.dialog_city_search_message, weatherParams.city))
                .setPositiveButton(getString(R.string.dialog_button_ok)) { dialog, _ ->
                    //viewModel.saveToDB(weatherParams)
                    saveCityInDataBase(weatherParams)
                    addItemOnListWeather(weatherParams)
                    dialog.dismiss()
                }
                .setNegativeButton(getString(R.string.dialog_button_no)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun addItemOnListWeather(city: WeatherParams) {
        listWeatherParams.add(DetailsFragment.newInstance(city))
    }

    private fun saveCityInDataBase(city: WeatherParams) {
        val handlerThread = HandlerThread("MyThread2")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        handler.post {
            getHistoryDao().insert(
                HistoryEntity(
                    0,
                    city.city,
                    city.degrees,
                    city.weatherCondition
                )
            )
        }
    }
}
