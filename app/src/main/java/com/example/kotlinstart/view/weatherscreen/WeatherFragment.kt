package com.example.kotlinstart.view.weatherscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.kotlinstart.KotlinStartApplication.Companion.getGeolocationHelper
import com.example.kotlinstart.R
import com.example.kotlinstart.databinding.FragmentWeatherBinding
import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.view.detailsscreen.DetailsFragment
import com.google.android.material.bottomappbar.BottomAppBar


class WeatherFragment : Fragment() {

    private lateinit var viewModel: WeatherViewModel
    private var weatherBinding: FragmentWeatherBinding? = null
    private val binding get() = weatherBinding!!
    private lateinit var adapter: WeatherAdapter
    private var weatherList: ArrayList<Weather> = ArrayList()
    private val myGeolocation = getGeolocationHelper()

    private val onClickListItem: OnClickItem = object : OnClickItem {

        override fun onClick(weather: Weather) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.list_container, DetailsFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        weatherBinding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.subscribe().observe(viewLifecycleOwner) { renderData(it) }
        viewModel.createWeatherData(weatherList)
        changeFabIcon()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        myGeolocation.checkPermissionsResult(requireContext(), requestCode, grantResults)
    }

    private fun renderData(weatherList: ArrayList<Weather>) {
        adapter = WeatherAdapter(weatherList, onClickListItem)
        this.weatherList = weatherList

        binding.recyclerViewMain.adapter = adapter
        ItemTouchHelper(ItemTouchHelperCallback(adapter))
            .attachToRecyclerView(binding.recyclerViewMain)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        weatherBinding = null
    }

    private fun changeFabIcon() {
        binding.bottomAppBar.navigationIcon = null
        binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END

        binding.fab.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_back
            )
        )
        binding.bottomAppBar.replaceMenu(R.menu.search_menu)
        clickOnFab()
        searchCity()
    }

    private fun clickOnFab() {
        // todo разобраться с действием назад
        binding.fab.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.list_container,DetailsFragment())
                .commitAllowingStateLoss()
        }
    }


   private fun searchCityDialog(weatherParams: WeatherParams) {
       if (weatherParams.city.isBlank()) {
           Toast.makeText(requireContext(), "Список адресов пустой", Toast.LENGTH_SHORT).show()
       } else {
           AlertDialog.Builder(requireContext())
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

    private fun addItemOnListWeather(city: WeatherParams) {
        weatherList.add(Weather(city.city, "", city.degrees))

    }
    private fun saveCityInDataBase(city: WeatherParams) {
        viewModel.saveCityInDataBase(city)
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
                        viewModel.getAddress(requireContext(), newText)
                    }
                }
                return true
            }
        })
    }

    interface OnClickItem {
        fun onClick(weather: Weather)
    }
}


