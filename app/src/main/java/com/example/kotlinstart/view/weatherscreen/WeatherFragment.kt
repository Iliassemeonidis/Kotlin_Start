package com.example.kotlinstart.view.weatherscreen

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
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
import com.example.kotlinstart.view.location.GeolocationHelper
import com.example.kotlinstart.view.location.REQUEST_CODE
import com.example.kotlinstart.view.location.RequestPermission
import com.example.kotlinstart.view.search.CityDialogFragment
import com.example.kotlinstart.view.shared.SharedViewModel


private const val SEARCH_CITY_TAG = "SEARCH_CITY_TAG"

class WeatherFragment : Fragment(), RequestPermission {

    private lateinit var viewModel: WeatherViewModel
    private var weatherBinding: FragmentWeatherBinding? = null
    private val binding get() = weatherBinding!!
    private lateinit var adapter: WeatherAdapter
    private lateinit var weatherList: ArrayList<Weather>
    private lateinit var myGeolocation: GeolocationHelper

    private val callBackDialog: CallBackDialog = object : CallBackDialog {
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
                        myGeolocation.requestPermission(this@WeatherFragment)
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

        override fun getContextFragment(): Context {
            return requireContext()
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

    private val onClickListItem: OnClickItem = object : OnClickItem {

        override fun onClick(weather: Weather) {
            // openWeatherDetails(weather)
        }
    }

//    fun openWeatherDetails(weather: Weather) {
//        requireActivity().supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.list_container, DetailsFragment.newInstance(weather.cityName))
//            .addToBackStack(null)
//            .commitAllowingStateLoss()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        myGeolocation = GeolocationHelper(callBackDialog)
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
//            requireContext().startActivity(Intent(requireContext(), ContactsActivity::class.java))
            CityDialogFragment().show(requireActivity().supportFragmentManager, SEARCH_CITY_TAG)
        }
    }

    private fun initButtonLocation() {
        binding.mainFragmentFABLocation.setOnClickListener {
            myGeolocation.checkPermission(requireContext(), this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        myGeolocation.checkPermissionsResult(requireContext(), requestCode, grantResults)
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

    interface CallBackDialog {
        fun showDialogGeolocationIsClosed()
        fun showDialogGeolocationIsDisabled()
        fun showRationaleDialog()
        fun showAddressDialog(city: String)
        fun getContextFragment(): Context
        fun alertDialog()
        fun getRequestPermissionRationale():Boolean
    }

    override fun requestPermission() {
        requireActivity().requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE
        )
    }
}
