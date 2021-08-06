package com.example.kotlinstart.view.detailsscreen

import android.app.IntentService
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.kotlinstart.constant.*
import com.example.kotlinstart.dto.FactDTO
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

    var city: String? = null

    private val callBack = object : retrofit2.Callback<WeatherDTO> {
        val intent = Intent(ACTION)

        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            if (response.isSuccessful) {
                intent.putExtra(
                    BROADCAST_WEATHER_DTO, convertDtoToWeatherData(city!!, response.body()!!)
                )
                LocalBroadcastManager.getInstance(this@DetailsService).sendBroadcast(intent)
            }
        }

        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            intent.putExtra(BROADCAST_WEATHER_DTO_EXCEPTION, t.message)
            LocalBroadcastManager.getInstance(this@DetailsService).sendBroadcast(intent)
        }
    }

    private fun convertDtoToWeatherData(city: String, weatherDTO: WeatherDTO): WeatherDetailsData {
        val fact: FactDTO = weatherDTO.fact!!
        return WeatherDetailsData(
            city,
            fact.temp!!.toString(),
            fact.condition!!,
            fact.feels_like!!.toString(),
            cityIconURL = getIconByCity(city),
            icon = "https://yastatic.net/weather/i/icons/blueye/color/svg/${fact.icon!!}.svg"
        )
    }

    private fun getIconByCity(city: String) = when (city) {
        "Москва" -> MOSCOW
        "Афины" -> ATHENS
        "Сочи" -> SOCHI
        "Лос-Анджелес" -> LOS_ANGELES
        "Владикавказ" -> VLADIKAVKAZ
        "Таймырский" -> TAIMIR
        else -> ""
    }

    override fun onHandleIntent(intent: Intent?) {
        city = intent?.getStringExtra(DETAILS_SERVICE_STRING_EXTRA)
        val detailsRepository = RepositoryDetailsImpl(RemoteDataSource())
        intent?.let {
            detailsRepository.getWeatherDataFromServers(
                it.getDoubleExtra(LATITUDE_EXTRA, 0.0),
                it.getDoubleExtra(LONGITUDE_EXTRA, 0.0),
                callBack
            )
        }
    }

    private fun createLogMessage(message: String) {
        Log.d(TAG, message)
    }
}