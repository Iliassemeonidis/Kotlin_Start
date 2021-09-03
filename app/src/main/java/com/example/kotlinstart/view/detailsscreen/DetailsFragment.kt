package com.example.kotlinstart.view.detailsscreen

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import coil.api.load
import com.example.kotlinstart.R
import com.example.kotlinstart.databinding.FragmentDetailsBinding
import com.example.kotlinstart.model.AppState
import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.model.getDetailWeather
import com.example.kotlinstart.view.experiments.MainBroadcastReceiver
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlinx.android.synthetic.main.fragment_details.*

const val ACTION = "Receive weather info sdfghsrdfgh"
const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
const val DETAILS_DATA_EMPTY_EXTRA = "DATA IS EMPTY"
const val DETAILS_RESPONSE_EMPTY_EXTRA = "RESPONSE IS EMPTY"
const val DETAILS_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"
const val DETAILS_TEMP_EXTRA = "TEMPERATURE"
const val DETAILS_FEELS_LIKE_EXTRA = "FEELS LIKE"
const val DETAILS_CONDITION_EXTRA = "CONDITION"

internal class DetailsFragment : Fragment() {

    private lateinit var detailsViewModel: DetailsViewModel
    private var detailsBinding: FragmentDetailsBinding? = null
    private val binding get() = detailsBinding!!
    private lateinit var weatherBundle: WeatherParams
    private val connectionReceiver: MainBroadcastReceiver = MainBroadcastReceiver()
    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.getParcelableExtra<WeatherDetailsData>(BROADCAST_WEATHER_DTO)?.let {
                binding.loadingLayout.visibility = View.GONE
                binding.textViewCityName.text = it.city
                binding.degrees.text = String.format("${R.string.degrees}",it.degrees)
                binding.weatherCondition.text = it.condition
                binding.textViewFeelsLike.text =String.format("${R.string.fills_like}",it.feelsLike)
                GlideToVectorYou.justLoadImage(
                    requireActivity(),
                    Uri.parse(it.icon),
                    icon_condition
                )
                binding.imageView.load(it.cityIconURL)
            }
        }
    }


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
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(receiver, IntentFilter(ACTION))
                it.registerReceiver(connectionReceiver, IntentFilter(CONNECTIVITY_ACTION))

        }
        detailsViewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherBundle = arguments?.getParcelable(CITY_EXTRA) ?: WeatherParams()
//        val weather = getDetailWeather(arguments?.getString(CITY_EXTRA) ?: DEFAULT_CITY)
        val weather :WeatherParams = arguments?.getParcelable(CITY_EXTRA) ?: WeatherParams()
        detailsViewModel.setNewCity(weather.city)
        detailsViewModel.getLiveData().observe(viewLifecycleOwner, { renderData(it) })
        detailsViewModel.getWeatherFromRemoteSource(weather.lat, weather.lon)
        /*requireActivity().startService(Intent(requireContext(), DetailsService::class.java).apply {
            putExtra(DETAILS_SERVICE_STRING_EXTRA, weather.city)
            putExtra(LATITUDE_EXTRA, weather.lat)
            putExtra(LONGITUDE_EXTRA, weather.lon)
        })*/
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                val weatherData = appState.weatherDetailsData
                binding.textViewCityName.text = weatherData.city
                binding.degrees.text = String.format(
                    getString(R.string.degrees_text),
                    appState.weatherDetailsData.degrees
                )
                binding.weatherCondition.text = appState.weatherDetailsData.condition
                binding.textViewFeelsLike.text =
                    "Ощущается как ${appState.weatherDetailsData.feelsLike}°"
                GlideToVectorYou.justLoadImage(
                    requireActivity(),
                    Uri.parse(appState.weatherDetailsData.icon),
                    icon_condition
                )
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
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(receiver)
            it.unregisterReceiver(connectionReceiver)
        }
        detailsBinding = null
        super.onDestroy()
    }

    companion object {

        const val CITY_EXTRA = "CITY_EXTRA"
        const val DEFAULT_CITY: String = "DEFAULT_CITY"

        @JvmStatic
        fun newInstance(city: WeatherParams) =
            DetailsFragment().apply { arguments = bundleOf(CITY_EXTRA to city) }
    }
}

