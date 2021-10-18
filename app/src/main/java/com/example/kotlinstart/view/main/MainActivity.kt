package com.example.kotlinstart.view.main

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinstart.R
import com.example.kotlinstart.databinding.ActivityMainBinding
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.view.detailsscreen.DetailsFragment
import com.example.kotlinstart.view.detailsscreen.DetailsViewPagerAdapter
import com.example.kotlinstart.view.weatherscreen.WeatherFragment
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.activity_main.*

//По ДЗ:
//- Переход с Main на Weather через FAB.+
// Возврат обратно по кнопке меню справа.+/-
//- Реализовать сохранение данных через VM +/-
//- Использовать AppState в качестве Unidirectional data flow-
//- Формировать ViewPager из БД+/-

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
        viewModel.subscribeOnWeatherParams().observe(this@MainActivity) { renderData(it) }
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
        formListFromDatabase()
        val adapter = DetailsViewPagerAdapter(
            this,
            listWeatherParams
        )
        pager.adapter = adapter
    }

    private fun formListFromDatabase() {
        viewModel.getWeatherParamsFromDataBase(listWeatherParams)

        //TODO разобраться почему при заполненом списке получаем этот тост
        if (listWeatherParams.isEmpty()) {
            Toast.makeText(this, "Список городов пуст", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initNavigationIcon() {
        binding.fab.setOnClickListener {
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
                changeFabIcon(isMain)
            } else {
                changeFabIcon(isMain)
            }
        }
    }

    private fun changeFabIcon(isMain: Boolean) {
        if (isMain) {
            this.isMain = false
            binding.bottomAppBar.navigationIcon = null
            binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END

            binding.fab.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_back
                )
            )
            binding.bottomAppBar.replaceMenu(R.menu.search_menu)
        } else {
            this.isMain = true
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
                    saveCityInDataBase(weatherParams)
                    addItemOnListWeather(weatherParams)
                    dialog.dismiss()
                }
                .setNegativeButton(getString(R.string.dialog_button_no)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    //TODO уточнить надо ли переность это во вью модель
    private fun addItemOnListWeather(city: WeatherParams) {
        listWeatherParams.add(DetailsFragment.newInstance(city))
    }

    private fun saveCityInDataBase(city: WeatherParams) {
        viewModel.saveCityInDataBase(city)
    }
}
