package com.example.kotlinstart.view.location

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.kotlinstart.R
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.view.weatherscreen.WeatherFragment
import java.io.IOException
import java.util.*

private const val REQUEST_CODE = 102
private const val REFRESH_PERIOD = 60000L
private const val MINIMAL_DISTANCE = 100f

//TODO Remove LC components from Constructor+
//TODO Remove dialogs+
class MyGeolocationHelper(
    private val callBackDialog: WeatherFragment.CallBackDialog
) {
    private val onLocationListener: LocationListener =
        LocationListener { location ->
            getAddressAsync(
                callBackDialog.getContextFragment(),
                location = location
            )
        }

    fun checkPermission(context: Context, fragment: Fragment) {
        context.let {
            when {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    getLocation(context, fragment)
                }
                fragment.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showRationaleDialog()
                }
                else -> {
                    requestPermission(fragment)
                }
            }
        }
    }

    fun requestPermission(fragment: Fragment) {
        fragment.requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE
        )
    }

    fun checkPermissionsResult(
        context: Context, fragment: Fragment,
        requestCode: Int,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                var grantedPermissions = 0
                if ((grantResults.isNotEmpty())) {
                    grantResults.map {
                        if (it == PackageManager.PERMISSION_GRANTED) grantedPermissions++
                    }
                    if (grantResults.size == grantedPermissions) {
                        getLocation(context, fragment)
                    } else {
                        callBackDialog.showDialog(
                            fragment.getString(R.string.dialog_title_no_gps),
                            fragment.getString(R.string.dialog_message_no_gps)
                        )
                    }
                } else {
                    callBackDialog.showDialog(
                        fragment.getString(R.string.dialog_title_no_gps),
                        fragment.getString(R.string.dialog_message_no_gps)
                    )
                }
            }
        }
    }

    private fun showDialog(
        title: String,
        message: String
    ) {
        callBackDialog.showDialog(title, message)
    }

    private fun showRationaleDialog() {
        callBackDialog.showRationaleDialog()
    }

    private fun getLocation(context: Context, fragment: Fragment) {
        context.let {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
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
                        context.let {
                            callBackDialog.alertDialog()
                        }
                    } else {
                        getAddressAsync(context, location)
                        showDialog(
                            fragment.getString(R.string.dialog_title_gps_turned_off),
                            fragment.getString(R.string.dialog_message_last_known_location)
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
        try {
            val addresses = geoCoder.getFromLocationName("Москва", 5)
            if (addresses.isNotEmpty()) {
                // TODO реализовать передачу обьекта WeatherParams
                showAddressDialog(addresses[0].getAddressLine(0), location)
            } else {
                Log.i("ADDRESS", "Список адресов пустой")
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun showAddressDialog(
        address: String,
        location: Location,
//        weatherParams: WeatherParams
    ) {
        WeatherParams(address, lat = location.latitude, lon = location.longitude)
        callBackDialog.showAddressDialog(address)
    }
}