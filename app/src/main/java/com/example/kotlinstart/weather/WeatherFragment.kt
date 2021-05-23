package com.example.kotlinstart.weather

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinstart.Communicator
import com.example.kotlinstart.R

internal class WeatherFragment : Fragment() {

    private lateinit var communicator: Communicator

    /*fun getCommunicator():Communicator = communicator
    fun setCommunicator(communicator: Communicator) {this.communicator = communicator}
    */
    private val onClickItem: OnClickItem = object : OnClickItem {
        override fun onClick(weather: Weather) {
            communicator.passDataComm(weather)
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
        createList(view)
    }

    private fun createList(view: View) {
        //Временно, сделанно для примера заполняние,а не как реализация
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view_main)
        val cityArray = resources.getStringArray(R.array.city).toList()
        val regionArray = resources.getStringArray(R.array.region).toList()
        val weather = ArrayList<Weather>()

        if (cityArray.size == regionArray.size) {
            for (i in cityArray.indices) {
                weather.add(Weather(cityArray[i], regionArray[i], "27°C"))
            }
        }
        recyclerView.adapter = WeatherAdapter(weather, onClickItem)
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
