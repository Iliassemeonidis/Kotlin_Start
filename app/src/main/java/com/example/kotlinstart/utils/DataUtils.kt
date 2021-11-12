package com.example.kotlinstart.utils

import com.example.kotlinstart.constant.*
import com.example.kotlinstart.dto.FactDTO
import com.example.kotlinstart.dto.WeatherDTO
import com.example.kotlinstart.view.detailsscreen.DetailsFragmentState
import com.example.kotlinstart.view.detailsscreen.WeatherDetailsData
import retrofit2.Response

private const val CORRUPTED_DATA = "Неполные данные"
private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"

internal fun getStateOnResponse(city: String, response: Response<WeatherDTO>): DetailsFragmentState {
    val serverResponse: WeatherDTO? = response.body()
    return if (response.isSuccessful && serverResponse != null) {
        checkResponse(city, response.body())
    } else {
        DetailsFragmentState.Error(Throwable(SERVER_ERROR))
    }
}

internal fun getStateOnFailure(t: Throwable) = DetailsFragmentState.Error(
    Throwable(t.message ?: REQUEST_ERROR)
)

internal fun checkResponse(city: String, serverResponse: WeatherDTO?): DetailsFragmentState {
    val fact = serverResponse?.fact
    return if (
        fact?.temp == null ||
        fact.feels_like == null ||
        fact.condition.isNullOrEmpty()
    ) {
        DetailsFragmentState.Error(Throwable(CORRUPTED_DATA))
    } else DetailsFragmentState.Success(convertDtoToWeatherData(city, serverResponse))
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



