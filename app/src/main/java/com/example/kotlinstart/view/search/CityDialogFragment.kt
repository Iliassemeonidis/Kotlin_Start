package com.example.kotlinstart.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinstart.databinding.FragmentCityDialogBinding
import com.example.kotlinstart.model.CityData
import java.util.*

internal class CityDialogFragment : DialogFragment() {

    private lateinit var cityDialogViewModel: CityDialogViewModel
    private var cityDialogBinding: FragmentCityDialogBinding? = null
    private val binding get() = cityDialogBinding!!


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
    }

    private fun renderData(it: ArrayList<CityData>) {
        binding.recyclerViewCitySearch.adapter = CityDialogAdapter(it)
    }

    interface OnClickCity {
        fun onClick(cityData: CityData)
    }
}
