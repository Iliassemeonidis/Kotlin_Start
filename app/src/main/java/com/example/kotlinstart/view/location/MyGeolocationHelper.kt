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
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.kotlinstart.R
import java.io.IOException

private const val REQUEST_CODE = 102
private const val REFRESH_PERIOD = 60000L
private const val MINIMAL_DISTANCE = 100f

class MyGeolocationHelper(
    private val context: Context,
    private val fragment: Fragment,
    private val activity: Activity
) {

    private val onLocationListener = object : LocationListener {

        override fun onLocationChanged(location: Location) {
            getAddressAsync(location)
        }

        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    fun checkPermission() {
        context.let {
            when {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    getLocation()
                }
                fragment.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                    showRationaleDialog()
                }
                else -> {
                    requestPermission()
                }
            }
        }
    }

    private fun requestPermission() {
        activity.requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_CODE
        )
    }

    fun checkPermissionsResult(
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
                        getLocation()
                    } else {
                        showDialog(
                            fragment.getString(R.string.dialog_title_no_gps),
                            fragment.getString(R.string.dialog_message_no_gps)
                        )
                    }
                } else {
                    showDialog(
                        fragment.getString(R.string.dialog_title_no_gps),
                        fragment.getString(R.string.dialog_message_no_gps)
                    )
                }
                return
            }
        }
    }

    private fun showDialog(
        title: String,
        message: String
    ) {
        context.let {
            AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(fragment.getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun showRationaleDialog() {
        context.let {
            AlertDialog.Builder(it)
                .setTitle(fragment.getString(R.string.dialog_rationale_title))
                .setMessage(fragment.getString(R.string.dialog_message_no_gps))
                .setPositiveButton(fragment.getString(R.string.dialog_rationale_give_access)) { _, _ ->
                    requestPermission()
                    ContextCompat.startActivity(
                        context,
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        },
                        null
                    )
                }
                .setNegativeButton(fragment.getString(R.string.dialog_rationale_decline)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun getLocation() {
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
                            AlertDialog.Builder(it)
                                .setTitle(fragment.getString(R.string.dialog_title_gps_turned_off))
                                .setMessage(fragment.getString(R.string.dialog_message_last_location_unknown))
                                .setPositiveButton("OK") { _, _ ->
                                    ContextCompat.startActivity(
                                        context,
                                        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                                        null
                                    )
                                }
                                .setNegativeButton(fragment.getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                                .create()
                                .show()
                        }

                    } else {
                        getAddressAsync(location)
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

    private fun getAddressAsync(location: Location) {
        val geoCoder = Geocoder(context)
        val handlerThread = HandlerThread("Geolocation")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        try {
            //getFromLocationName
            val addresses = geoCoder.getFromLocation(
                37.4220,
                -122.0840,
                1
            )
            println(addresses)
//                val addresses = geoCoder.getFromLocationName(cityName,5)

            handler.post {
                showAddressDialog(addresses[0].getAddressLine(0), location)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun showAddressDialog(
        address: String,
        location: Location,
    ) {
        context.let {
            AlertDialog.Builder(it)
                .setTitle(fragment.getString(R.string.dialog_address_title))
                .setMessage(address)
                .setPositiveButton(fragment.getString(R.string.dialog_address_get_weather)) { _, _ ->
                    //openDetailsScreen(location)
                }
                .setNegativeButton(fragment.getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

}