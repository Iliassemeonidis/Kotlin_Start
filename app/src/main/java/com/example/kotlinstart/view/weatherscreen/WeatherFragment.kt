package com.example.kotlinstart.view.weatherscreen

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.kotlinstart.R
import com.example.kotlinstart.databinding.FragmentWeatherBinding
import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.view.contacts.ContactsActivity
import com.example.kotlinstart.view.detailsscreen.DetailsFragment
import com.example.kotlinstart.view.location.MyGeolocationHelper
import com.example.kotlinstart.view.shared.SharedViewModel

/*
по ДЗ:
- Рефактор MyGeolocationHelper: перенести код UI во фрагмент, прокинуть каллбэк, избавиться
- добавить функционал по поиску координат по адресу
- передавать в DetailsFragment координаты

- проверить кейс: диалоговое окно с адресом не открывается при первом разрешении на GPS
*/

class WeatherFragment : Fragment() {

    private lateinit var viewModel: WeatherViewModel
    private var weatherBinding: FragmentWeatherBinding? = null
    private val binding get() = weatherBinding!!
    private lateinit var adapter: WeatherAdapter
    private lateinit var weatherList: ArrayList<Weather>
    private lateinit var myGeolocation: MyGeolocationHelper

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
        myGeolocation = MyGeolocationHelper(requireContext(), this, requireActivity())
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
        //subscribeToSharedViewModel()
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
        }
    }

    private fun initButtonLocation() {
        binding.mainFragmentFABLocation.setOnClickListener {
            /*requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )

*/
            myGeolocation.checkPermission()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        myGeolocation.checkPermissionsResult(requestCode, grantResults)
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
