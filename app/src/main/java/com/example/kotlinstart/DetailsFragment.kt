package com.example.kotlinstart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kotlinstart.weather.Weather
import com.example.kotlinstart.weather.WeatherFragment
import kotlinx.android.synthetic.main.fragment_details.*

internal class DetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val weather: Weather? = arguments?.getParcelable(WeatherFragment.PERSON_KEY)
        text_view_city_name.text = weather?.cityName
        degrees.text = weather?.temperature
        weather_condition.text = "Солнечно"
        text_view_feels_like.text = "Ощущается как 27°"
    }
}
