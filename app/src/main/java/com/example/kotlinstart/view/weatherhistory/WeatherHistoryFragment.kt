package com.example.kotlinstart.view.weatherhistory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.kotlinstart.databinding.FragmentWeatherHistoryBinding
import com.example.kotlinstart.room.HistoryEntity
import com.example.kotlinstart.view.weatherscreen.ItemTouchHelperCallback


class WeatherHistoryFragment : Fragment() {

    private lateinit var weatherHistoryViewModel: WeatherHistoryViewModel
    private var weatherHistoryBinding: FragmentWeatherHistoryBinding? = null
    private val binding get() = weatherHistoryBinding!!
    private lateinit var adapter:WeatherHistoryAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        weatherHistoryViewModel = ViewModelProvider(this).get(WeatherHistoryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        weatherHistoryBinding = FragmentWeatherHistoryBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherHistoryViewModel.subscribe().observe(viewLifecycleOwner, { renderData(it) })
        weatherHistoryViewModel.getCitiesList()
    }

    private fun renderData(weatherList: List<HistoryEntity>) {
        adapter = WeatherHistoryAdapter(weatherList)
        binding.recyclerWeatherHistory.adapter = adapter
    }

}