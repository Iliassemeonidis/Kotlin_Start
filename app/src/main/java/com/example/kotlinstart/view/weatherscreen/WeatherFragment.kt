package com.example.kotlinstart.view.weatherscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinstart.R
import com.example.kotlinstart.databinding.FragmentWeatherBinding
import com.example.kotlinstart.view.data.Weather
import com.example.kotlinstart.view.detailsscreen.DetailsFragment
import com.example.kotlinstart.view.search.CityDialogFragment

internal class WeatherFragment : Fragment() {

    private lateinit var viewModel: WeatherViewModel
    private var weatherBinding: FragmentWeatherBinding? = null
    private val binding get() = weatherBinding!!

    private val onClickListItem: OnClickItem = object : OnClickItem {
        override fun onClick(weather: Weather) {
            openWeatherDetails(weather)
        }

        private fun openWeatherDetails(weather: Weather) {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.list_container, DetailsFragment.newInstance(weather.cityName))
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
        viewModel.subscribe().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getCitiesList()
        initButtonAdd()
    }

    private fun initButtonAdd() {
        binding.floatingActionButton.setOnClickListener {
            CityDialogFragment().show(requireActivity().supportFragmentManager, SEARCH_CITY_TAG)
        }
    }

    private fun renderData(weatherList: ArrayList<Weather>) {
        binding.recyclerViewMain.adapter = WeatherAdapter(weatherList, onClickListItem)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        weatherBinding = null
    }

    companion object {
        private const val SEARCH_CITY_TAG = "SEARCH_CITY_TAG"
    }

    interface OnClickItem {
        fun onClick(weather: Weather)
    }
}
