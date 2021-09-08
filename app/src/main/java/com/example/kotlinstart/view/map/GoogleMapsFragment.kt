package com.example.kotlinstart.view.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.kotlinstart.R
import com.example.kotlinstart.model.WeatherParams
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class GoogleMapsFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->

        val weatherParams = arguments?.getParcelable(WEATHER_PARAMS) ?: WeatherParams()
        val latLng = LatLng(weatherParams.lat, weatherParams.lon)
        googleMap.addMarker(
            MarkerOptions().position(latLng).title(weatherParams.city)
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))

        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        googleMap.uiSettings.isZoomControlsEnabled = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_google_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    companion object {
        private const val WEATHER_PARAMS = "WEATHER_PARAMS"

        fun newInstance(weatherParams: WeatherParams) =
            GoogleMapsFragment().apply { arguments = bundleOf(WEATHER_PARAMS to weatherParams) }
    }
}