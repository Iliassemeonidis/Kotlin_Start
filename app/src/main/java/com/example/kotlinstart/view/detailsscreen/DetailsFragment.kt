package com.example.kotlinstart.view.detailsscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinstart.databinding.FragmentDetailsBinding

internal class DetailsFragment : Fragment() {

    private lateinit var detailsViewModel: DetailsViewModel
    private var detailsBinding: FragmentDetailsBinding? = null
    private val binding get() = detailsBinding!!

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
        detailsViewModel = ViewModelProvider(
            this,
            DetailsFactory(city = arguments?.getString(CITY_EXTRA) ?: DEFAULT_CITY)
        ).get(DetailsViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailsViewModel.subscribe().observe(viewLifecycleOwner, { renderData(it) })
        detailsViewModel.getCityName()
    }

    private fun renderData(city: String?) {
        city?.let {
            binding.textViewCityName.text = it
            binding.degrees.text = "27°"
            binding.weatherCondition.text = "Солнечно"
            binding.textViewFeelsLike.text = "Ощущается как 27°"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        detailsBinding = null
    }

    companion object {

        const val CITY_EXTRA = "CITY_EXTRA"
        const val DEFAULT_CITY: String = "DEFAULT_CITY"

        @JvmStatic
        fun newInstance(city: String) =
            DetailsFragment().apply { arguments = bundleOf(CITY_EXTRA to city) }
    }
}