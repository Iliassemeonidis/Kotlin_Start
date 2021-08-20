package com.example.kotlinstart.view.detailsscreen

import android.os.Handler
import android.os.HandlerThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinstart.KotlinStartApplication.Companion.getHistoryDao
import com.example.kotlinstart.dto.WeatherDTO
import com.example.kotlinstart.model.AppState
import com.example.kotlinstart.repository.detailsrepository.RepositoryDetailsImpl
import com.example.kotlinstart.repository.detailsrepository.datasource.RemoteDataSource
import com.example.kotlinstart.room.HistoryEntity
import com.example.kotlinstart.utils.getStateOnFailure
import com.example.kotlinstart.utils.getStateOnResponse
import retrofit2.Call
import retrofit2.Response

internal class DetailsViewModel(
    private val detailsLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepository: RepositoryDetailsImpl = RepositoryDetailsImpl(RemoteDataSource())
) : ViewModel() {

    lateinit var city: String
    private val callBack = object : retrofit2.Callback<WeatherDTO> {

        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            val stateSuccess = getStateOnResponse(city, response)
            if (stateSuccess is AppState.Success) {
                val data = stateSuccess.weatherDetailsData

                val handlerThread = HandlerThread("MyThread2")
                handlerThread.start()
                val handler = Handler(handlerThread.looper)
                handler.post{
                    getHistoryDao().insert(
                        HistoryEntity(
                            0,
                            data.city,
                            data.degrees,
                            data.condition
                        )
                    )
                }
//                Thread {
//                    getHistoryDao().insert(
//                        HistoryEntity(
//                            0,
//                            data.city,
//                            data.degrees,
//                            data.condition
//                        )
//                    )
//                }.start()
            }
            detailsLiveData.postValue(stateSuccess)
        }

        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            detailsLiveData.postValue(getStateOnFailure(t))
        }
    }

    fun setNewCity(city: String) {
        this.city = city
    }

    fun getLiveData() = detailsLiveData

    fun getWeatherFromRemoteSource(lat: Double, lon: Double) {
        detailsLiveData.value = AppState.Loading
        detailsRepository.getWeatherDataFromServers(lat, lon, callBack)
    }
}

