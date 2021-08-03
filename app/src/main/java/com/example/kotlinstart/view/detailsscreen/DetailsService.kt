package com.example.kotlinstart.view.detailsscreen

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.example.kotlinstart.dto.WeatherDTO
import com.example.kotlinstart.repository.detailsrepository.RepositoryDetailsImpl
import com.example.kotlinstart.repository.loader.RemoteDataSource
import retrofit2.Call
import retrofit2.Response

private const val TAG = "DetailsServiceTAG"
const val DETAILS_SERVICE_STRING_EXTRA = "DetailsServiceExtra"
const val BROADCAST_WEATHER_DTO = "WeatherDTO"
const val BROADCAST_WEATHER_DTO_EXCEPTION = "Exception"
const val LATITUDE_EXTRA = "Latitude"
const val LONGITUDE_EXTRA = "Longitude"

class DetailsService(name: String = "DetailsService") : IntentService(name) {

    private val callBack = object : retrofit2.Callback<WeatherDTO> {
        // не совсем понимаю, что тут я должен передавать...
        val intent = Intent(DETAILS_INTENT_FILTER)

        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            if (response.isSuccessful) {
                intent.putExtra(BROADCAST_WEATHER_DTO, response)
                sendBroadcast(intent)
            }
        }

        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            intent.putExtra(BROADCAST_WEATHER_DTO_EXCEPTION, t.message)
            sendBroadcast(intent)
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        createLogMessage("onHandleIntent${intent?.getStringExtra(DETAILS_SERVICE_STRING_EXTRA)}")
        val myIntent = Intent(DETAILS_INTENT_FILTER)
        //sendBroadcast(myIntent)

        val detailsRepository = RepositoryDetailsImpl(RemoteDataSource())
        intent?.let {
            detailsRepository.getWeatherDataFromServers(
                it.getDoubleExtra(LATITUDE_EXTRA,0.0),
                it.getDoubleExtra(LONGITUDE_EXTRA,0.0),
                callBack
            )
        }
    }

    private fun createLogMessage(message: String) {
        Log.d(TAG, message)
    }
}