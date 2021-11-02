package com.example.kotlinstart.view.mainscreen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.example.kotlinstart.GeolocationInterface
import com.example.kotlinstart.KotlinStartApplication.Companion.getGeolocationHelper
import com.example.kotlinstart.R
import com.example.kotlinstart.databinding.FragmentMainBinding
import com.example.kotlinstart.location.PermissionInterface
import com.example.kotlinstart.location.REQUEST_CODE
import com.example.kotlinstart.model.AppState
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.model.WeatherParamsInterface
import com.example.kotlinstart.view.weatherscreen.WeatherFragment
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

const val ACTION = "Receive"

class MainFragment : Fragment(),
    PermissionInterface, GeolocationInterface, WeatherParamsInterface {

    private lateinit var mainViewModel: MainViewModel
    private var detailsBinding: FragmentMainBinding? = null
    private val binding get() = detailsBinding!!
    private val myGeolocation = getGeolocationHelper()

    override fun requestPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        myGeolocation.checkPermissionsResult(requireContext(), requestCode, grantResults)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        detailsBinding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        myGeolocation.listener = this
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setParamsInModel(arguments?.getParcelable(CITY_EXTRA) ?: WeatherParams("Москва"))
    }


    override fun onDestroy() {
        detailsBinding = null
        myGeolocation.listener = null
        super.onDestroy()
    }

//    private fun checkPermissions() {
//        Snackbar.make(
//            this.requireView(),
//            "Определить погоду по Вашей геолокации?",
//            6000
//        ).setAction("Определить") {
//            myGeolocation.checkPermission(requireContext(), this)
//        }
//            .show()
//    }

    private fun setParamsInModel(weatherParams: WeatherParams) {
        mainViewModel.setNewCity(weatherParams.city)
        mainViewModel.getLiveData().observe(viewLifecycleOwner) { renderData(it) }
        mainViewModel.getWeatherFromRemoteSource(weatherParams.lat, weatherParams.lon)
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
                binding.textViewFeelsLike.text = String.format(
                    getString(R.string.fills_like),
                    appState.weatherDetailsData.feelsLike
                )
                GlideToVectorYou.justLoadImage(
                    requireActivity(),
                    Uri.parse(appState.weatherDetailsData.icon),
                    binding.iconCondition
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
            else -> Toast.makeText(
                requireContext(),
                R.string.massage_error_appstate,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun showDialogGeolocationIsClosed() {
        requireContext().let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_title_no_gps))
                .setMessage(getString(R.string.dialog_message_no_gps))
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    override fun showDialogGeolocationIsDisabled() {
        requireContext().let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_title_gps_turned_off))
                .setMessage(getString(R.string.dialog_message_last_known_location))
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    override fun showRationaleDialog() {
        context?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_rationale_title))
                .setMessage(getString(R.string.dialog_message_no_gps))
                .setPositiveButton(getString(R.string.dialog_rationale_give_access)) { _, _ ->
                    myGeolocation.requestPermission(this@MainFragment)
                    openAppSettingsPermission(it)
                }
                .setNegativeButton(getString(R.string.dialog_rationale_decline)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    override fun showAddressDialog(city: String) {
        context?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(city)
                .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                    //openWeatherDetails(Weather(city))
                }
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }


    override fun alertDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_title_gps_turned_off))
            .setMessage(getString(R.string.dialog_message_last_location_unknown))
            .setPositiveButton("OK") { _, _ ->
                ContextCompat.startActivity(
                    requireContext(),
                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                    null
                )
            }
            .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }

    override fun getRequestPermissionRationale() =
        shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)


    override fun getWeatherParamsFromUserLocation(params: WeatherParams) {
        setParamsInModel(params)
    }

    companion object {

        const val CITY_EXTRA = "CITY_EXTRA"

        @JvmStatic
        fun newInstance(city: WeatherParams) =
            MainFragment().apply { arguments = bundleOf(CITY_EXTRA to city) }
    }

    private fun openAppSettingsPermission(it: Context) {
        ContextCompat.startActivity(
            it,
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", it.packageName, null)
            },
            null
        )
    }
    override fun getCreatedListWeather(): ArrayList<WeatherParams> {
        TODO("Not yet implemented")
    }
}
