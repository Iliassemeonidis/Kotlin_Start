package com.example.kotlinstart.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.kotlinstart.GeolocationInterface
import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.view.base.baseinterface.OnGetAddressListener
import java.io.IOException
import java.util.*

const val REQUEST_CODE = 102
private const val REFRESH_PERIOD = 60000L
private const val MINIMAL_DISTANCE = 100f

class GeolocationHelper(context: Context) {

    var listener: GeolocationInterface? = null

    private val onLocationListener: LocationListener =
        LocationListener { location ->
            getAddressAsync(
                context,
                location = location
            )
        }

    fun checkPermission(context: Context, permissionInterface: PermissionInterface) {
        context.let {
            when {
                ContextCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    getLocation(context)
                }
                listener!!.getRequestPermissionRationale() -> {
                    listener?.showRationaleDialog()
                }
                else -> {
                    requestPermission(permissionInterface)

                }
            }
        }
    }

    fun requestPermission(permission: PermissionInterface) {
        permission.requestPermission()
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
                        listener?.showDialogGeolocationIsClosed()
                    }
                } else {
                    listener?.showDialogGeolocationIsClosed()
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
                            listener?.alertDialog()
                        }
                    } else {
                        getAddressAsync(context, location)
                        listener?.showDialogGeolocationIsDisabled()
                    }
                }
            } else {
                listener!!.showRationaleDialog()
            }
        }
    }

    private fun getAddressAsync(context: Context, location: Location) {
        val geoCoder = Geocoder(context)
        try {
            // для примера указан город
            val addresses = geoCoder.getFromLocation(location.latitude, location.longitude, 1)
            if (addresses.isNotEmpty()) {
//                showAddressDialog(addresses[0].getAddressLine(0), location)
                listener?.getWeatherParamsFromUserLocation(
                    WeatherParams(
                        addresses[0].locality,
                        lat = location.latitude,
                        lon = location.longitude
                    )
                )
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
        if (listener == null) throw NullPointerException("GeolocationInterface must be initialized")
        listener!!.showAddressDialog(address)
    }

    fun getAddressAsyncByCity(
        context: Context,
        city: String,
        addressListener: OnGetAddressListener
    ) {
        val geoCoder = Geocoder(context)
//        try {
//            val addresses = geoCoder.getFromLocationName(city, 1)
            val addresses = "Moscow"
            addressListener.onValidData(Weather(addresses))
//
//            if (addresses.isNotEmpty()) {
//                if (addresses[0].locality.isEmpty()) {
//                    addressListener.onEmpty()
//                } else {
//                    addressListener.onValidData(Weather(addresses[0].locality))
//                }
//            } else {
//                addressListener.onEmpty()
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//            addressListener.onError(e)
//        }
    }
}