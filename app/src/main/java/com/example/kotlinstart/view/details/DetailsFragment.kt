package com.example.kotlinstart.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kotlinstart.R
import com.example.kotlinstart.view.data.Weather
import com.example.kotlinstart.view.main.WeatherFragment
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
        weather?.let {
            text_view_city_name.text = it.cityName
            degrees.text = it.temperature
            weather_condition.text = "Солнечно"
            text_view_feels_like.text = "Ощущается как 27°"
        }
    }
}
