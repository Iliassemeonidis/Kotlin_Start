package com.example.kotlinstart.view.experiments

import android.app.IntentService
import android.content.Intent
import android.util.Log
import com.example.kotlinstart.dto.WeatherDTO
import com.example.kotlinstart.repository.detailsrepository.RepositoryDetailsImpl
import com.example.kotlinstart.repository.loader.RemoteDataSource
import com.example.kotlinstart.view.shared.MainActivity
import retrofit2.Call
import retrofit2.Response

private const val TAG = "MainServiceTAG"
const val MAIN_SERVICE_STRING_EXTRA = "MainServiceExtra"

class MainService(name: String = "MainService") : IntentService(name) {

    private val callBack = object : retrofit2.Callback<WeatherDTO> {

        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            //Send broadcast
            //val intentActivity = Intent(this@MainService, MainActivity::class.java)
            val intent = Intent("sdfgsedfgsdf")
            //intent.putExtra("MSG", response)
            sendBroadcast(intent)
        }

        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            //Send broadcast
        }
    }

    override fun onHandleIntent(intent: Intent?) {
        createLogMessage("onHandleIntent${intent?.getStringExtra(MAIN_SERVICE_STRING_EXTRA)}")
        val intent = Intent("sdfgsedfgsdf")
        //intent.putExtra("MSG", response)
        sendBroadcast(intent)

        val detailsRepository = RepositoryDetailsImpl(
            RemoteDataSource()
        )
        /*intent?.let {
            detailsRepository.getWeatherDataFromServers(
                it.getDoubleExtra(LAT),
                it.getDoubleExtra(LON),
                callBack
            )
        }*/
    }

    override fun onCreate() {
        createLogMessage("onCreate")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createLogMessage("onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        createLogMessage("onDestroy")
        super.onDestroy()
    }

    private fun createLogMessage(message: String) {
        Log.d(TAG, message)
    }
}