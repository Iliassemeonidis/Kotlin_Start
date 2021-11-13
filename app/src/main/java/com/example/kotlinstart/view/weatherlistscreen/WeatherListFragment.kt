package com.example.kotlinstart.view.weatherlistscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.kotlinstart.KotlinStartApplication.Companion.getGeolocationHelper
import com.example.kotlinstart.R
import com.example.kotlinstart.databinding.FragmentWeatherListBinding
import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.view.detailsscreen.DetailsFragment
import com.example.kotlinstart.view.detailsscreen.SearchCityState
import com.example.kotlinstart.view.mainscreen.MainFragment
import com.google.android.material.bottomappbar.BottomAppBar

class WeatherListFragment : Fragment() {

    private lateinit var viewModel: WeatherListViewModel
    private var weatherBinding: FragmentWeatherListBinding? = null
    private val binding get() = weatherBinding!!
    private lateinit var listAdapter: WeatherListAdapter
    private val myGeolocation = getGeolocationHelper()
    private var isAttached = true

    private val onClickListItem: OnClickItem = object : OnClickItem {

        override fun onClick(position:Int) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, MainFragment.newInstance(position))
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherListViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isAttached = true
        weatherBinding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    //TODO разобраться с очередностью вызова методов
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeOnViewModel()

        listAdapter = WeatherListAdapter(onClickListItem)

        binding.recyclerViewMain.adapter = listAdapter

        ItemTouchHelper(ItemTouchHelperCallback(listAdapter))
            .attachToRecyclerView(binding.recyclerViewMain)

        initFab()
        onClickFab()
    }

    private fun subscribeOnViewModel() {
        viewModel.subscribeToNewAddress().observe(viewLifecycleOwner) { onWeatherItemAdded(it) }
        viewModel.subscribeToDB().observe(viewLifecycleOwner) { onWeatherListAdded(it) }
        viewModel.subscribeToNewCity().observe(viewLifecycleOwner) { isCityReady(it) }
        viewModel.getWeatherFromBD()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        weatherBinding = null
        listAdapter.onDestroy()
    }

    private fun onWeatherListAdded(list: MutableList<Weather>) {
        listAdapter.onListAdded(list) }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        myGeolocation.checkPermissionsResult(requireContext(), requestCode, grantResults)
    }

    private fun onWeatherItemAdded(weather: Weather) {
        if (isAttached) {
            isAttached = false
        } else {
            if (weather.cityName.isNotEmpty()) {
                listAdapter.onItemAdded(weather)

                /*viewModel.saveCityInDataBase(WeatherParams().apply {
                    city = weather.cityName
                })*/
            }
        }
    }

    private fun addItemOnListWeather(city: WeatherParams) {
        //weatherList.add(Weather(city.city, "", city.degrees))
    }

    private fun saveCityInDataBase(city: WeatherParams) {
        //viewModel.saveCityInDataBase(city)
    }

    interface OnClickItem {
        fun onClick(position:Int)
    }

    private fun initFab() {
        binding.bottomAppBar.navigationIcon = null
        binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
        binding.fab.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_back
            )
        )
        binding.bottomAppBar.replaceMenu(R.menu.search_menu)
        searchCity()
    }

    private fun onClickFab() {
        binding.fab.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.main_container,MainFragment())
                .commitAllowingStateLoss()
        }
    }

    private fun searchCity() {
        val search = binding.bottomAppBar.menu.getItem(0).actionView as SearchView
        search.queryHint = getString(R.string.city_search)

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

    private fun isCityReady(state: SearchCityState) {
                when (state) {
            is SearchCityState.Success -> showDialog(state.city)
            SearchCityState.Empty -> Toast.makeText(
                requireContext(),
                R.string.city_is_not_find,
                Toast.LENGTH_SHORT
            ).show()
            is SearchCityState.Error -> TODO()
        }
    }

    private fun showDialog(city: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.botton_search))
            .setMessage(getString(R.string.dialog_city_search_message, city))
            .setPositiveButton(getString(R.string.dialog_button_ok)) { dialog, _ ->
                viewModel.onCityApprovedByUser(Weather(city))
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.dialog_button_no)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}