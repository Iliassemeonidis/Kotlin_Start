package com.example.kotlinstart.view.weatherscreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinstart.databinding.FragmentWeatherBinding
import com.example.kotlinstart.view.Navigator
import com.example.kotlinstart.view.data.Weather
import com.example.kotlinstart.view.search.CityFragment

internal class WeatherFragment : Fragment() {

    private lateinit var navigator: Navigator
    private lateinit var viewModel: WeatherViewModel
    private var weatherBinding: FragmentWeatherBinding? = null
    private val binding get() = weatherBinding!!

    private val onClickListItem: OnClickItem = object : OnClickItem {

        override fun onClick(weather: Weather) {
            navigator.openWeatherDetails(weather)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        navigator = context as Navigator
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
        //val listener = Observer<ArrayList<Weather>> { renderData(it) }
        viewModel.subscribe().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getCitiesList()
        initButtonAdd()
    }

    private fun initButtonAdd() {
        // todo пределать так чтоб открывался не новый фрагмент, а DialogFragment
        binding.floatingActionButton.setOnClickListener { navigator.openNewFragment(CityFragment()) }
    }

    private fun renderData(weatherList: ArrayList<Weather>) {
        binding.recyclerViewMain.adapter = WeatherAdapter(weatherList, onClickListItem)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        weatherBinding = null
    }

    interface OnClickItem {
        fun onClick(weather: Weather)
    }
}
