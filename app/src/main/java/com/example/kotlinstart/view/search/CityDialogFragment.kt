package com.example.kotlinstart.view.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinstart.DialogInterface
import com.example.kotlinstart.databinding.FragmentCityDialogBinding
import com.example.kotlinstart.model.CityData
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.view.main.SharedViewModel
import java.util.*

class CityDialogFragment : DialogFragment() {

    private lateinit var cityDialogViewModel: CityDialogViewModel
    private var cityDialogBinding: FragmentCityDialogBinding? = null
    private val binding get() = cityDialogBinding!!
    private var weatherParams: WeatherParams = WeatherParams()
    private val liveDataForObservation: MutableLiveData<String> = MutableLiveData()

    fun subscribe(): LiveData<String> {
        return liveDataForObservation
    }

//    private val callBackDialog: CallBackDialog = object : CallBackDialog {
//        override fun getWeatherParams(weather: WeatherParams) {
//            weatherParams = weather
//        }
//
//        override fun openDetalisationOfCity() {
//            requireActivity().supportFragmentManager
//                .beginTransaction()
//                .replace(
//                    R.id.list_container,
//                    GoogleMapsFragment.newInstance(weatherParams)
//                )
//                .addToBackStack(null)
//                .commitAllowingStateLoss()
//            closeDialogFragment()
//        }
//
//        override fun getContext(): Context {
//            return requireContext()
//        }
//    }

    private val onClickCity: OnClickCity = object : OnClickCity {
        override fun onClick(cityData: CityData) {
            // Save city item in pref
            saveItemToSharedViewModel(cityData.cityName)
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

    private fun saveItemToSharedViewModel(cityName: String) {
        val sharedViewModel: SharedViewModel by activityViewModels()
        sharedViewModel.setCity(cityName)
    }

    interface OnClickCity {
        fun onClick(cityData: CityData)
    }

    private fun initButtonSearch() {
        binding.buttonSearch.setOnClickListener {
            /*val helper = getGeolocationHelper()
            helper.listener = callBackDialog*/
           // GeolocationHelper.getAddressAsync(callBackDialog, binding.searchCity.text.toString())
        }
    }

    interface CallBackDialog: DialogInterface {
        fun getWeatherParams(weather: WeatherParams)
        fun openDetalisationOfCity()
        fun getContext(): Context
    }
}

