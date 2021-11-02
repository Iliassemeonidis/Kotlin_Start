package com.example.kotlinstart.view.base

import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinstart.R
import com.example.kotlinstart.databinding.ActivityMainBinding
import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.view.base.baseinterface.DialogSearchInterface
import com.example.kotlinstart.view.mainscreen.MainFragment
import com.example.kotlinstart.view.mainscreen.MainViewPagerAdapter
import com.example.kotlinstart.view.weatherscreen.WeatherFragment
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.synthetic.main.activity_main.*

//По ДЗ:

//- Разобраться с видимость FAB
// ( сделал в activity_main.xml FrameLayout в который кладу фрагменты) +

//- Реализовать сохранение данных через VM в MainFragment
// (вынес вечь функционал по добавлению в BaseViewModel и в WeatherFragment по подписке добавляю и схораняю)+

//- Использовать AppState в качестве Unidirectional data flow и
// возвращать корректные ошибки (не использую ,
// по той причине что не понял сути их применения , реализовал все через лисенер интерфейса DialogSearchInterface)

//- Доделать остальные TODO +(за исключением некоторых не методов)


// разобраться с действием назад после -
//- добавления города и отработать нажатие на элименты списка(дописал себе задание сам)



internal class BaseActivity : AppCompatActivity(), DialogSearchInterface {

    private lateinit var viewModel: BaseViewModel
    private lateinit var mainBinding: ActivityMainBinding
    private val binding get() = mainBinding
    private lateinit var adapter: MainViewPagerAdapter
    private var isMain = true
    private var isExit = false

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewMainParams()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this).inflate(R.menu.menu_bottom_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_favourite -> { openWeatherFragment() }
            R.id.app_bar_settings -> Toast.makeText(this, R.string.settings, Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
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

    private fun initViewMainParams() {
        viewModel = ViewModelProvider(this).get(BaseViewModel::class.java)
        viewModel.subscribeOnWeatherParams().observe(this@BaseActivity) { renderData(it) }
        viewModel.subscribeOnWeatherFromDB().observe(this) { onWeatherList(it) }
        viewModel.getWeatherParamsFromDataBase()

        initBindingAndPager()
    }

    private fun initBindingAndPager() {
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        initPagerAdapterAndInitBotomBar()
    }

    private fun initPagerAdapterAndInitBotomBar() {
        adapter = MainViewPagerAdapter(
            this,
            mutableListOf()
        )
        pager.adapter = adapter

        createBottomBarAndNavigationIcon()
    }

    private fun createBottomBarAndNavigationIcon() {
        setSupportActionBar(binding.bottomAppBar)
        binding.fab.setOnClickListener {
            if (isMain) {
                changeFabIcon(isMain)
                openWeatherFragment()
            } else {
                changeFabIcon(isMain)
                this.supportFragmentManager.popBackStack()
            }
        }
    }

    private fun openWeatherFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.list_container, WeatherFragment())
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    private fun onWeatherList(list: MutableList<MainFragment>) {
        if (list.isNotEmpty()) {
            binding.listEmptyTextView.visibility = View.INVISIBLE
        }
        adapter.addNewList(list)
    }

    private fun changeFabIcon(isMain: Boolean) {
        if (isMain) {
            changeFabSetSearchAndIconBack()
        } else {
            changeFabSetPlus()
        }
    }

    private fun changeFabSetSearchAndIconBack() {
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
        searchCity()
    }

    private fun changeFabSetPlus() {
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

    private fun checkExit() {
        when (isExit) {
            true -> {
                super.onBackPressed()
            }
            else -> {
                Toast.makeText(this, R.string.exit, Toast.LENGTH_SHORT).show()
                isExit = true
            }
        }
    }

    private fun renderData(weatherParams: WeatherParams) {
        if (weatherParams.city.isBlank()) {
            Toast.makeText(this, "Список адресов пустой", Toast.LENGTH_SHORT).show()
        } else {
            binding.listEmptyTextView.visibility = View.INVISIBLE
        }
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
                        viewModel.getAddress(this@BaseActivity, newText, this@BaseActivity)
                    }
                }
                return true
            }
        })
    }

    override fun showDialog(city: String) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.botton_search))
            .setMessage(getString(R.string.dialog_city_search_message, city))
            .setPositiveButton(getString(R.string.dialog_button_ok)) { dialog, _ ->
                viewModel.saveWeather(Weather(city))
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.dialog_button_no)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun showToast() {
        Toast.makeText(this, R.string.city_is_not_find, Toast.LENGTH_SHORT).show()
    }
}
