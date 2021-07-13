package com.example.kotlinstart.utils

import com.example.kotlinstart.dto.FactDTO
import com.example.kotlinstart.dto.WeatherDTO
import com.example.kotlinstart.model.AppState
import com.example.kotlinstart.model.WeatherData
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

private const val CORRUPTED_DATA = "Неполные данные"
private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"

fun Date.myFormat(): String {
    return SimpleDateFormat("yyyy.dd.MMM mm:HH", Locale.getDefault()).format(this)
}

internal fun getStateOnResponse(city: String, response: Response<WeatherDTO>): AppState {
    val serverResponse: WeatherDTO? = response.body()
    return if (response.isSuccessful && serverResponse != null) {
        checkResponse(city, response.body())
    } else {
        AppState.Error(Throwable(SERVER_ERROR))
    }
}

internal fun getStateOnFailure(t: Throwable) = AppState.Error(
    Throwable(t.message ?: REQUEST_ERROR)
)

internal fun checkResponse(city: String, serverResponse: WeatherDTO?): AppState {
    val fact = serverResponse?.fact
    return if (
        fact?.temp == null ||
        fact.feels_like == null ||
        fact.condition.isNullOrEmpty()
    ) {
        AppState.Error(Throwable(CORRUPTED_DATA))
    } else AppState.Success(convertDtoToWeatherData(city, serverResponse))
}

private fun convertDtoToWeatherData(city: String, weatherDTO: WeatherDTO): WeatherData {
    val fact: FactDTO = weatherDTO.fact!!
    return WeatherData(
        city,
        "${fact.temp!!}°C",
        fact.condition!!,
        "Ощущается как ${fact.feels_like!!}°",
    )
}
