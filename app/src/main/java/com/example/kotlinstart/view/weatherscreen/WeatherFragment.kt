package com.example.kotlinstart.view.weatherscreen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.kotlinstart.R
import com.example.kotlinstart.databinding.FragmentWeatherBinding
import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.view.contacts.ContactsActivity
import com.example.kotlinstart.view.detailsscreen.DetailsFragment
import com.example.kotlinstart.view.shared.SharedViewModel
import java.io.IOException

/*
по ДЗ:
- открывать настройки приложения для разрешения Fine Location
- можно включить GPS программно? или открыть соответствующее окно настроек
- добавить функционал по поиску координат по адресу
- передавать в DetailsFragment координаты
- проверить кейс: диалоговое окно с адресом не открывается при первом разрешении на GPS
- перенести код в Репозиторий
*/

private const val REQUEST_CODE = 102
private const val REFRESH_PERIOD = 60000L
private const val MINIMAL_DISTANCE = 100f

class WeatherFragment : Fragment() {

    private lateinit var viewModel: WeatherViewModel
    private var weatherBinding: FragmentWeatherBinding? = null
    private val binding get() = weatherBinding!!
    private lateinit var adapter: WeatherAdapter
    private lateinit var weatherList: ArrayList<Weather>
    private val onLocationListener = object : LocationListener {

        override fun onLocationChanged(location: Location) {
            context?.let {
                getAddressAsync(it, location)
            }
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private val onClickListItem: OnClickItem = object : OnClickItem {
        override fun onClick(weather: Weather) {
            openWeatherDetails(weather)
        }

        private fun openWeatherDetails(weather: Weather) {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.list_container, DetailsFragment.newInstance(weather.cityName))
                .addToBackStack(null)
                .commitAllowingStateLoss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        weatherBinding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.subscribe().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getCitiesList()
        subscribeToSharedViewModel()
        initButtonAdd()
        initButtonLocation()
    }

    private fun subscribeToSharedViewModel() {
        val sharedViewModel: SharedViewModel by activityViewModels()
        sharedViewModel.subscribe().observe(viewLifecycleOwner, {
            adapter.setItemInList(Weather(it))
        })
    }

    private fun initButtonAdd() {
        binding.floatingActionButton.setOnClickListener {
            requireContext().startActivity(Intent(requireContext(), ContactsActivity::class.java))

            /*requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.list_container, WeatherHistoryFragment())
                    .addToBackStack(null)
                    .commitAllowingStateLoss()*/
            //CityDialogFragment().show(requireActivity().supportFragmentManager, SEARCH_CITY_TAG)
        }
    }

    private fun initButtonLocation() {
        binding.mainFragmentFABLocation.setOnClickListener {
            checkPermission()
        }
    }

    private fun checkPermission() {
        activity?.let {
            when {
                ContextCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED -> {
                    getLocation()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showRationaleDialog()
                }
                else -> {
                    requestPermission()
                }
            }
        }

    }

    private fun requestPermission() {
        requireActivity().requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        checkPermissionsResult(requestCode, grantResults)
    }

    private fun checkPermissionsResult(requestCode: Int, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE -> {
                var grantedPermissions = 0
                if ((grantResults.isNotEmpty())) {
                    grantResults.map {
                        if (it == PackageManager.PERMISSION_GRANTED) grantedPermissions++
                    }
/*
                    for (i in grantResults) {
                        if (i == PackageManager.PERMISSION_GRANTED) {
                            grantedPermissions++
                        }
                    }*/
                    if (grantResults.size == grantedPermissions) {
                        getLocation()
                    } else {
                        showDialog(
                            getString(R.string.dialog_title_no_gps),
                            getString(R.string.dialog_message_no_gps)
                        )
                    }
                } else {
                    showDialog(
                        getString(R.string.dialog_title_no_gps),
                        getString(R.string.dialog_message_no_gps)
                    )
                }
                return
            }
        }
    }

    private fun showDialog(title: String, message: String) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun showRationaleDialog() {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_rationale_title))
                .setMessage(getString(R.string.dialog_rationale_meaasge))
                .setPositiveButton(getString(R.string.dialog_rationale_give_access)) { _, _ ->
                    requestPermission()
                }
                .setNegativeButton(getString(R.string.dialog_rationale_decline)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun getLocation() {
        activity?.let { context ->
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                // Получить менеджер геолокаций
                val locationManager =
                    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    val provider = locationManager.getProvider(LocationManager.GPS_PROVIDER)
                    provider?.let {
                        // Будем получать геоположение через каждые 60 секунд или каждые 100 метров
                        locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            REFRESH_PERIOD,
                            MINIMAL_DISTANCE,
                            onLocationListener
                        )
                    }
                } else {
                    val location =
                        locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                    if (location == null) {
                        showDialog(
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_location_unknown)
                        )
                    } else {
                        getAddressAsync(context, location)
                        showDialog(
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_known_location)
                        )
                    }
                }
            } else {
                showRationaleDialog()
            }
        }
    }

    private fun getAddressAsync(context: Context, location: Location) {
        val geoCoder = Geocoder(context)
        Thread {
            try {
                //getFromLocationName
                val addresses = geoCoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )
                binding.root.post {
                    showAddressDialog(addresses[0].getAddressLine(0), location)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }.start()
    }

    private fun showAddressDialog(address: String, location: Location) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(getString(R.string.dialog_address_title))
                .setMessage(address)
                .setPositiveButton(getString(R.string.dialog_address_get_weather)) { _, _ ->
                    //openDetailsScreen(location)
                }
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun renderData(weatherList: ArrayList<Weather>) {
        adapter = WeatherAdapter(weatherList, onClickListItem)
        this.weatherList = weatherList

        binding.recyclerViewMain.adapter = adapter
        ItemTouchHelper(ItemTouchHelperCallback(adapter))
            .attachToRecyclerView(binding.recyclerViewMain)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        weatherBinding = null
    }

    interface OnClickItem {
        fun onClick(weather: Weather)
    }
}
