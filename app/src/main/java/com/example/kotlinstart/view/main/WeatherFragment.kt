package com.example.kotlinstart.view.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinstart.view.search.CityFragment
import com.example.kotlinstart.R
import com.example.kotlinstart.view.Communicator
import com.example.kotlinstart.view.data.Weather
import kotlinx.android.synthetic.main.fragment_weather.*

internal class WeatherFragment : Fragment() {

    private lateinit var communicator: Communicator

    /*fun getCommunicator():Communicator = communicator
    fun setCommunicator(communicator: Communicator) {this.communicator = communicator}
    */
    private val onClickItem: OnClickItem = object : OnClickItem {

        override fun onClick(weather: Weather) {
            communicator.openWeatherDetails(weather)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        communicator = context as Communicator
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createList()
        initButtonAdd()
    }

    private fun initButtonAdd() {
        floating_action_button.setOnClickListener{communicator.openNewFragment(CityFragment())}
    }

    private fun createList() {
        //Временно, сделанно для примера заполняние,а не как реализация
        val cityArray = resources.getStringArray(R.array.city).toList()
        val regionArray = resources.getStringArray(R.array.region).toList()
        val weather = ArrayList<Weather>()

        if (cityArray.size == regionArray.size) {
            for (i in cityArray.indices) {
                weather.add(Weather(cityArray[i], regionArray[i], "27°C"))
            }
        }
        recycler_view_main.adapter = WeatherAdapter(weather, onClickItem)
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
