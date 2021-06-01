package com.example.kotlinstart.view.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinstart.databinding.FragmentWeatherBinding
import com.example.kotlinstart.view.Communicator
import com.example.kotlinstart.view.data.Weather
import com.example.kotlinstart.view.search.CityFragment
import com.example.kotlinstart.view.ui.MainViewModel

internal class WeatherFragment : Fragment() {

    private lateinit var communicator: Communicator
    private lateinit var viewModel: MainViewModel
    private var weatherBinding: FragmentWeatherBinding? = null
    private val binding get() = weatherBinding!!

    private val onClickItem: OnClickItem = object : OnClickItem {

        override fun onClick(weather: Weather) {
            communicator.openWeatherDetails(weather)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        communicator = context as Communicator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
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
        val observer = Observer<Any> { renderData(it) }
        viewModel.getData().observe(viewLifecycleOwner, observer)
        createList(viewModel.getData().value as ArrayList<Weather>)
        initButtonAdd()
    }

    private fun initButtonAdd() {
        // todo пределать так чтоб открывался не новый фрагмент, а DialogFragment
        binding.floatingActionButton.setOnClickListener { communicator.openNewFragment(CityFragment()) }
    }

    private fun createList(weather: ArrayList<Weather>) {
        binding.recyclerViewMain.adapter = WeatherAdapter(weather, onClickItem)
    }

    // не понимаю реализацию, типа после изменение д
    private fun renderData(data: Any) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        weatherBinding = null
    }

    companion object {

        const val PERSON_KEY = "personKye"

        @JvmStatic
        fun newInstance(counter: Int) =
            WeatherFragment().apply { arguments = bundleOf(PERSON_KEY to counter) }
    }

    interface OnClickItem {
        fun onClick(weather: Weather)
    }
}
