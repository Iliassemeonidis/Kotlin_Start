package com.example.kotlinstart.view.detailsscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinstart.dto.FactDTO
import com.example.kotlinstart.dto.WeatherDTO
import com.example.kotlinstart.model.AppState
import com.example.kotlinstart.model.WeatherData
import com.example.kotlinstart.repository.loader.RemoteDataSource
import com.example.kotlinstart.repository.detailsrepository.RepositoryDetailsImpl
import retrofit2.Call
import retrofit2.Response

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

internal class DetailsViewModel(
    private val detailsLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepository: RepositoryDetailsImpl = RepositoryDetailsImpl(RemoteDataSource())
) : ViewModel() {

    lateinit var city: String

    fun setNewCity(city: String) {
        this.city = city
    }

    fun getLiveData() = detailsLiveData

    fun getWeatherFromRemoteSource( lat: Double, lon: Double) {
        detailsLiveData.value = AppState.Loading
        detailsRepository.getWeatherDataFromServers(lat, lon, callBack)
    }


    private val callBack = object : retrofit2.Callback<WeatherDTO> {

        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            val serverResponse: WeatherDTO? = response.body()
            detailsLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(response.body())
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))

                }
            )

        }

        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            detailsLiveData.postValue(
                AppState.Error(
                    Throwable(
                        t.message ?: REQUEST_ERROR
                    )
                )
            )
        }

    }

    private fun checkResponse(serverResponse: WeatherDTO?): AppState {
        val fact = serverResponse?.fact
        return if (fact?.temp == null || fact.feels_like ==
            null || fact.condition.isNullOrEmpty()
        ) {
            AppState.Error(Throwable(CORRUPTED_DATA))
        } else AppState.Success(convertDtoToModel(serverResponse))
    }

    private fun convertDtoToModel(weatherDTO: WeatherDTO): WeatherData {
        val fact: FactDTO = weatherDTO.fact!!
        return WeatherData(city,"${fact.temp!!}°C", fact.condition!!,"Ощущается как ${fact.feels_like!!}°",)
    }
}

