package com.example.kotlinstart.view.detailsscreen

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.bumptech.glide.Glide
import com.example.kotlinstart.databinding.FragmentDetailsBinding
import com.example.kotlinstart.model.AppState
import com.example.kotlinstart.model.getDetailWeather
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlinx.android.synthetic.main.fragment_details.*

internal class DetailsFragment : Fragment() {

    private lateinit var detailsViewModel: DetailsViewModel
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailsViewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weather = getDetailWeather(arguments?.getString(CITY_EXTRA) ?: DEFAULT_CITY)
        detailsViewModel.setNewCity(weather.city)
        detailsViewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        detailsViewModel.getWeatherFromRemoteSource(weather.lat, weather.lon)
    }

    @SuppressLint("SetTextI18n")
    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                val weatherData = appState.weatherDetailsData
                binding.textViewCityName.text = weatherData.city
                binding.degrees.text = "${appState.weatherDetailsData.degrees}°"
                binding.weatherCondition.text = appState.weatherDetailsData.condition
                binding.textViewFeelsLike.text = "Ощущается как ${appState.weatherDetailsData.feelsLike}°"
                GlideToVectorYou.justLoadImage(requireActivity(), Uri.parse(appState.weatherDetailsData.icon),icon_condition)
                binding.imageView.load(appState.weatherDetailsData.cityIconURL)
            }
            is AppState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Toast.makeText(requireContext(), appState.error.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        detailsBinding = null
    }

    companion object {

        const val CITY_EXTRA = "CITY_EXTRA"
        const val DEFAULT_CITY: String = "DEFAULT_CITY"

        @JvmStatic
        fun newInstance(city: String) =
            DetailsFragment().apply { arguments = bundleOf(CITY_EXTRA to city) }
    }
}

