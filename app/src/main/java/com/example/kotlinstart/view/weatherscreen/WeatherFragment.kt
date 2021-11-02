package com.example.kotlinstart.view.weatherscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.kotlinstart.KotlinStartApplication.Companion.getGeolocationHelper
import com.example.kotlinstart.R
import com.example.kotlinstart.databinding.FragmentWeatherBinding
import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.view.base.MainViewModel
import com.example.kotlinstart.view.mainscreen.MainFragment

class WeatherFragment : Fragment() {

    private val viewModel: MainViewModel by activityViewModels()
    private var weatherBinding: FragmentWeatherBinding? = null
    private val binding get() = weatherBinding!!
    private lateinit var adapter: WeatherAdapter
    private val myGeolocation = getGeolocationHelper()
    private var isAttached = true

    private val onClickListItem: OnClickItem = object : OnClickItem {

        override fun onClick(weather: Weather) {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.list_container, MainFragment())
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isAttached = true
        weatherBinding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.subscribeToNewAddress().observe(viewLifecycleOwner) { onWeatherItemAdded(it) }
        viewModel.subscribeToDB().observe(viewLifecycleOwner) { onWeatherListAdded(it) }
        viewModel.getWeatherFromBD()

        adapter = WeatherAdapter(onClickListItem)
        binding.recyclerViewMain.adapter = adapter
        ItemTouchHelper(ItemTouchHelperCallback(adapter))
            .attachToRecyclerView(binding.recyclerViewMain)

        //changeFabIcon()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        weatherBinding = null
        adapter.onDestroy()
    }

    private fun onWeatherListAdded(list: MutableList<Weather>) {
        adapter.onListAdded(list)
    }

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
                adapter.onItemAdded(weather)

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
        fun onClick(weather: Weather)
    }
}


