package com.example.kotlinstart.view.weatherscreen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.kotlinstart.R
import com.example.kotlinstart.databinding.FragmentWeatherBinding
import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.location.GeolocationHelper
import com.example.kotlinstart.location.REQUEST_CODE
import com.example.kotlinstart.location.RequestPermission
import com.example.kotlinstart.view.map.GoogleMapsFragment
import com.example.kotlinstart.view.search.CityDialogFragment
import com.example.kotlinstart.view.shared.SharedViewModel

//По ДЗ:
//- Почитать про жизненный цикл View и методы ЖЦ
//- Прочитать методички из базового курса
//- Использовать полученные знания для улучшения UI/UX погодного приложения

private const val SEARCH_CITY_TAG = "SEARCH_CITY_TAG"

class WeatherFragment : Fragment() {

    private lateinit var viewModel: WeatherViewModel
    private var weatherBinding: FragmentWeatherBinding? = null
    private val binding get() = weatherBinding!!
    private lateinit var adapter: WeatherAdapter
    private lateinit var weatherList: ArrayList<Weather>
    private lateinit var myGeolocation: GeolocationHelper



    private val onClickListItem: OnClickItem = object : OnClickItem {

        override fun onClick(weather: Weather) {
            // openWeatherDetails(weather)
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
        viewModel.subscribe().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getCitiesList()
        subscribeToSharedViewModel()
        initButtonAdd()
    }

    private fun subscribeToSharedViewModel() {
        val sharedViewModel: SharedViewModel by activityViewModels()
        sharedViewModel.subscribe().observe(viewLifecycleOwner, {
            adapter.setItemInList(Weather(it))
        })
    }

    private fun initButtonAdd() {
        binding.floatingActionButton.setOnClickListener {
//            requireContext().startActivity(Intent(requireContext(), ContactsActivity::class.java))

            // open city search fragment
            CityDialogFragment().show(requireActivity().supportFragmentManager, SEARCH_CITY_TAG)
            // open google maps

        }
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

    interface OnClickItem {
        fun onClick(weather: Weather)
    }

}
