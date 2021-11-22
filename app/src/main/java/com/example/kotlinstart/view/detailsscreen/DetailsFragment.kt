package com.example.kotlinstart.view.detailsscreen

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
import com.example.kotlinstart.databinding.FragmentDetailsBinding
import com.example.kotlinstart.location.PermissionInterface
import com.example.kotlinstart.location.REQUEST_CODE
import com.example.kotlinstart.model.WeatherParams
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou

const val ACTION = "Receive"

class DetailsFragment : Fragment(),
    PermissionInterface, GeolocationInterface {

    private lateinit var mainViewModel: DetailsViewModel
    private var detailsBinding: FragmentDetailsBinding? = null
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
        detailsBinding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
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

    private fun renderData(detailsFragmentState: DetailsFragmentState) {
        when (detailsFragmentState) {
            is DetailsFragmentState.Success -> {
                binding.loadingLayout.visibility = View.GONE
                val weatherData = detailsFragmentState.weatherDetailsData
                binding.textViewCityName.text = weatherData.city
                binding.degrees.text = String.format(
                    getString(R.string.degrees_text),
                    detailsFragmentState.weatherDetailsData.degrees
                )
                binding.weatherCondition.text = detailsFragmentState.weatherDetailsData.condition
                binding.textViewFeelsLike.text = String.format(
                    getString(R.string.fills_like),
                    detailsFragmentState.weatherDetailsData.feelsLike
                )
                GlideToVectorYou.justLoadImage(
                    requireActivity(),
                    Uri.parse(detailsFragmentState.weatherDetailsData.icon),
                    binding.iconCondition
                )
                binding.imageView.load(detailsFragmentState.weatherDetailsData.cityIconURL)

            }
            is DetailsFragmentState.Loading -> {
                binding.loadingLayout.visibility = View.VISIBLE
            }
            is DetailsFragmentState.Error -> {
                binding.loadingLayout.visibility = View.GONE
                Toast.makeText(
                    requireContext(),
                    detailsFragmentState.error.message,
                    Toast.LENGTH_SHORT
                ).show()
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
                    myGeolocation.requestPermission(this@DetailsFragment)
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

        private fun openAppSettingsPermission(it: Context) {
            ContextCompat.startActivity(
                it,
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", it.packageName, null)
                },
                null
            )
        }

        @JvmStatic
        fun newInstance(city: WeatherParams) =
            DetailsFragment().apply { arguments = bundleOf(CITY_EXTRA to city) }
    }
}
