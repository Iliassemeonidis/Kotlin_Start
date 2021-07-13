package com.example.kotlinstart.view.detailsscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.example.kotlinstart.databinding.FragmentDetailsBinding
import com.example.kotlinstart.model.AppState
import com.example.kotlinstart.model.getDetailWeather

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

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                val weatherData = appState.weatherData
                binding.textViewCityName.text = weatherData.city
                binding.degrees.text = appState.weatherData.degrees
                binding.weatherCondition.text = appState.weatherData.weatherCondition
                binding.textViewFeelsLike.text = appState.weatherData.textViewFeelsLike
                binding.imageView.load("https://images-wixmp-ed30a86b8c4ca887773594c2.wixmp.com/f/d9b043f6-c6b3-4949-ba57-42e064d37ac5/d2pmhr3-ee361858-8935-457b-b4e4-4edb3bc4da4c.jpg?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1cm46YXBwOjdlMGQxODg5ODIyNjQzNzNhNWYwZDQxNWVhMGQyNmUwIiwiaXNzIjoidXJuOmFwcDo3ZTBkMTg4OTgyMjY0MzczYTVmMGQ0MTVlYTBkMjZlMCIsIm9iaiI6W1t7InBhdGgiOiJcL2ZcL2Q5YjA0M2Y2LWM2YjMtNDk0OS1iYTU3LTQyZTA2NGQzN2FjNVwvZDJwbWhyMy1lZTM2MTg1OC04OTM1LTQ1N2ItYjRlNC00ZWRiM2JjNGRhNGMuanBnIn1dXSwiYXVkIjpbInVybjpzZXJ2aWNlOmZpbGUuZG93bmxvYWQiXX0.ZSVPyRieDEHjTrNTcVNy6Tqkx83FHCQK9s9z20LJHEI")
                //binding.imageView.load("https://yastatic.net/weather/i/icons/blueye/color/svg/${weatherData.icon}.svg")
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

