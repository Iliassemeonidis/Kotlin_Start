package com.example.kotlinstart.repository.loader

import com.example.kotlinstart.BuildConfig
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request

object Loader {

    fun loadWeather(
        callback: Callback,
        lat: Double,
        lon: Double,
    ) {
        val client = OkHttpClient() // Клиент
        val builder: Request.Builder = Request.Builder() // Создаём строителя запроса
        builder.header("X-Yandex-API-Key", BuildConfig.WEATHER_API_KEY) // Создаём заголовок запроса
        builder.url("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}&lang=ru_RU") // Формируем URL
        val request: Request = builder.build() // Создаём запрос
        val call: Call = client.newCall(request) // Ставим запрос в очередь и отправляем
        call.enqueue(callback)
    }
}