package com.example.kotlinstart.view.weatherlistscreen

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.kotlinstart.KotlinStartApplication.Companion.getGeolocationHelper
import com.example.kotlinstart.R
import com.example.kotlinstart.databinding.FragmentWeatherListBinding
import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.view.detailsscreen.SearchCityState
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.android.parcel.Parcelize

internal class WeatherListFragment : Fragment() {

    private var weatherBinding: FragmentWeatherListBinding? = null
    private val binding get() = weatherBinding!!

    private lateinit var viewModel: WeatherListViewModel
    private lateinit var listAdapter: WeatherListAdapter
    private var isListSizeChanged = false

    private val onClickListItem: OnClickItem = object : OnClickItem {

        override fun onClick(position: Int) {
            when (isListSizeChanged) {
                true -> setFragmentResult(
                    REQUEST_KEY,
                    bundleOf(LIST_STATE_KEY to ListState.ToPosition(position, true))
                )
                else -> setFragmentResult(
                    REQUEST_KEY,
                    bundleOf(LIST_STATE_KEY to ListState.ToPosition(position, false))
                )
            }
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    checkStateList()
                }
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherListViewModel::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        weatherBinding = null
        listAdapter.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        weatherBinding = FragmentWeatherListBinding.inflate(inflater, container, false)
        return binding.root
    }

    //TODO разобраться с очередностью вызова методов
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeOnViewModel()

        initListAdapterAndRecyclerAdapter()

        ItemTouchHelper(ItemTouchHelperCallback(listAdapter)).attachToRecyclerView(binding.recyclerViewMain)

        initFab()

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        getGeolocationHelper().checkPermissionsResult(requireContext(), requestCode, grantResults)
    }

    private fun initListAdapterAndRecyclerAdapter() {
        listAdapter = WeatherListAdapter(onClickListItem)
        binding.recyclerViewMain.adapter = listAdapter
    }

    private fun subscribeOnViewModel() {
        viewModel.subscribeToDB().observe(viewLifecycleOwner) { onWeatherListAdded(it) }
        viewModel.subscribeToNewCity().observe(viewLifecycleOwner) { isCityReady(it) }
        viewModel.getWeatherFromBD()
    }

    private fun onWeatherListAdded(list: MutableList<Weather>) {
        listAdapter.onListAdded(list)
        isListAdapterEmpty()
    }

    private fun isListAdapterEmpty() {
        if (listAdapter.itemCount == 0) {
            Toast.makeText(requireContext(), R.string.add_new_city, Toast.LENGTH_SHORT).show()
        }
    }

    private fun initFab() {
        binding.bottomAppBar.navigationIcon = null
        binding.bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
        binding.fab.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_back
            )
        )
        binding.bottomAppBar.replaceMenu(R.menu.search_menu)
        onClickFab()
        searchCity()
    }

    private fun onClickFab() {
        binding.fab.setOnClickListener {
            checkStateList()
        }
    }

    private fun searchCity() {
        val search = binding.bottomAppBar.menu.getItem(0).actionView as SearchView
        search.queryHint = getString(R.string.city_search)

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    if (newText.length > 4) {
                        viewModel.getAddress(requireContext(), newText)
                    }
                }
                return true
            }
        })
    }

    private fun isCityReady(state: SearchCityState) {
        when (state) {
            is SearchCityState.Success -> showDialog(state.weather)
            SearchCityState.Empty -> Toast.makeText(
                requireContext(),
                R.string.city_is_not_find,
                Toast.LENGTH_SHORT
            ).show()
            is SearchCityState.Error ->
                Toast.makeText(requireContext(), state.error.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDialog(weather: WeatherParams) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.botton_search))
            .setMessage(getString(R.string.dialog_city_search_message, weather.city))
            .setPositiveButton(getString(R.string.dialog_button_ok)) { dialog, _ ->
                saveWeatherInDbAndAddToAdapter(weather, dialog)
            }
            .setNegativeButton(getString(R.string.dialog_button_no)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun saveWeatherInDbAndAddToAdapter(
        weather: WeatherParams,
        dialog: DialogInterface
    ) {
        viewModel.onCityApprovedByUser(weather)
        listAdapter.onItemAdded(Weather(weather.city))
        isListSizeChanged = true
        dialog.dismiss()
    }

    private fun checkStateList() {
        setFragmentResult(
            REQUEST_KEY,
            bundleOf(LIST_STATE_KEY to ListState.NotChanged(isListSizeChanged))
        )
        requireActivity().supportFragmentManager.popBackStack()
    }

    companion object {
        const val LIST_STATE_KEY = "LIST_STATE"
        const val REQUEST_KEY = "1"
    }

    interface OnClickItem {
        fun onClick(position: Int)
    }
}
