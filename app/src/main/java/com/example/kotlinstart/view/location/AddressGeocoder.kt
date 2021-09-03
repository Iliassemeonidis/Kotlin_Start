package com.example.kotlinstart.view.location

import android.content.Context
import android.location.Geocoder
import android.util.Log
import android.widget.Toast
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.view.search.CityDialogFragment
import java.io.IOException

internal class AddressGeocoder(private val callBackDialog: CityDialogFragment.CallBackDialog) {

    fun getAddressAsync(context: Context, city: String) {
        val geoCoder = Geocoder(context)
        try {
            val addresses = geoCoder.getFromLocationName(city, 5)
            if (addresses.isNotEmpty()) {
                callBackDialog.getWeatherParams(
                    WeatherParams(
                        city,
                        lat = addresses[0].latitude,
                        lon = addresses[0].longitude
                    )
                )
                callBackDialog.showDialog()
            } else {
                Toast.makeText(context, "Нет такого города", Toast.LENGTH_SHORT).show()
                Log.i("ADDRESS", "Список адресов пустой")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}