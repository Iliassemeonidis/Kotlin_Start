package com.example.kotlinstart.view.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.kotlinstart.databinding.FragmentDetailsBinding
import com.example.kotlinstart.view.data.Weather
import com.example.kotlinstart.view.main.WeatherFragment
import kotlinx.android.synthetic.main.fragment_details.*

internal class DetailsFragment : Fragment() {

    private var detailsBinding: FragmentDetailsBinding? = null
    private val binding get() = detailsBinding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        detailsBinding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        val weather: Weather? = arguments?.getParcelable(WeatherFragment.PERSON_KEY)
        weather?.let {
            binding.textViewCityName.text = it.cityName
            binding.degrees.text = it.temperature
            binding.weatherCondition.text = "Солнечно"
            binding.textViewFeelsLike.text = "Ощущается как 27°"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        detailsBinding = null
    }
}
