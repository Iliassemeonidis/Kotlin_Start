package com.example.kotlinstart.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinstart.R
import com.example.kotlinstart.databinding.FragmentCityDialogBinding
import com.example.kotlinstart.model.CityData
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.view.detailsscreen.DetailsFragment
import com.example.kotlinstart.view.location.AddressGeocoder
import com.example.kotlinstart.view.shared.SharedViewModel
import java.util.*

internal class CityDialogFragment : DialogFragment() {

    private lateinit var cityDialogViewModel: CityDialogViewModel
    private var cityDialogBinding: FragmentCityDialogBinding? = null
    private val binding get() = cityDialogBinding!!
    private var weatherParams: WeatherParams = WeatherParams()
    private lateinit var addressGeocoder: AddressGeocoder

    private val callBackDialog: CallBackDialog = object : CallBackDialog {
        override fun getWeatherParams(weather: WeatherParams) {
            weatherParams = weather
        }

        override fun showDialog() {
            context?.let {
                AlertDialog.Builder(it)
                    .setTitle("Поиск подходящего адресса")
                    .setMessage("Показать детализацию по запрошенному адрессу")
                    .setPositiveButton("OK") { _, _ ->
                        requireActivity().supportFragmentManager
                            .beginTransaction()
                            .replace(
                                R.id.list_container,
                                DetailsFragment.newInstance(weatherParams)
                            )
                            .addToBackStack(null)
                            .commitAllowingStateLoss()
                        closeDialogFragment()
                    }
                    .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
            }
        }
    }

    private val onClickCity: OnClickCity = object : OnClickCity {
        override fun onClick(cityData: CityData) {
            // Save city item in pref
            saveItemToSeredViewModel(cityData.cityName)
            //close this fragment
            closeDialogFragment()
        }
    }

    private fun closeDialogFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .remove(this@CityDialogFragment).commitAllowingStateLoss()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cityDialogViewModel = ViewModelProvider(this).get(CityDialogViewModel::class.java)
        addressGeocoder = AddressGeocoder(callBackDialog)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        cityDialogBinding = FragmentCityDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cityDialogViewModel.subscribe().observe(viewLifecycleOwner, { renderData(it) })
        cityDialogViewModel.getCityNamesList()
        initButtonSearch()
    }

    private fun renderData(it: ArrayList<CityData>) {
        //binding.recyclerViewCitySearch.adapter = CityDialogAdapter(it, onClickCity)
    }

    private fun saveItemToSeredViewModel(cityName: String) {
        val sharedViewModel: SharedViewModel by activityViewModels()
        sharedViewModel.setCity(cityName)
    }

    interface OnClickCity {
        fun onClick(cityData: CityData)
    }

    private fun initButtonSearch() {
        binding.buttonSearch.setOnClickListener {
            addressGeocoder.getAddressAsync(requireContext(), binding.searchCity.text.toString())
        }
    }

    interface CallBackDialog {
        fun getWeatherParams(weather: WeatherParams)
        fun showDialog()
    }
}

