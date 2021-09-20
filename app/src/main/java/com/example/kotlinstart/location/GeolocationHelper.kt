package com.example.kotlinstart.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.view.detailsscreen.DetailsFragment
import com.example.kotlinstart.view.search.CityDialogFragment
import com.example.kotlinstart.view.weatherscreen.WeatherFragment
import com.google.android.gms.maps.model.LatLng
import java.io.IOException

const val REQUEST_CODE = 102
private const val REFRESH_PERIOD = 60000L
private const val MINIMAL_DISTANCE = 100f

class GeolocationHelper(
    private val callBackDialog: DetailsFragment.CallBackDialog
) {
    private val onLocationListener: LocationListener =
        LocationListener { location ->
            getAddressAsync(
                callBackDialog.getContextFragment(),
                location = location
            )
        }

    fun checkPermission(context: Context, requestPermission: RequestPermission) {
        context.let {
            when {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    getLocation(context)
                }
                callBackDialog.getRequestPermissionRationale() -> {
                    callBackDialog.showRationaleDialog()
                }
                else -> {
                    requestPermission(requestPermission)
                }
            }
        }
    }

    fun requestPermission(requestPermission: RequestPermission) {
        requestPermission.requestPermission()
    }

    fun checkPermissionsResult(
        context: Context,
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
                        getLocation(context)
                    } else {
                        callBackDialog.showDialogGeolocationIsClosed()
                    }
                } else {
                    callBackDialog.showDialogGeolocationIsClosed()
                }
            }
        }
    }

    private fun getLocation(context: Context) {
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
                        callBackDialog.showDialogGeolocationIsDisabled()
                    }
                }
            } else {
                callBackDialog.showRationaleDialog()
            }
        }
    }

     private fun getAddressAsync(context: Context, location: Location) {
        val geoCoder = Geocoder(context)
        try {
            // для примера указан город
            val addresses = geoCoder.getFromLocation(location.latitude,location.longitude,1)
            if (addresses.isNotEmpty()) {
//                println(addresses[0].locality)
//                println(location)
//                showAddressDialog(addresses[0].getAddressLine(0), location)
                callBackDialog.getWeatherParamsFromUserLocation(WeatherParams(addresses[0].locality,lat = location.latitude,lon = location.longitude))
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
    ) {
        callBackDialog.showAddressDialog(address)
    }

    companion object {

        fun getAddressAsync(callBackDialog: CityDialogFragment.CallBackDialog, city: String) {
            val geoCoder = Geocoder(callBackDialog.getContext())
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
                    callBackDialog.openDetalisationOfCity()
                } else {
                    Toast.makeText(
                        callBackDialog.getContext(),
                        "Нет такого города",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.i("ADDRESS", "Список адресов пустой")
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}