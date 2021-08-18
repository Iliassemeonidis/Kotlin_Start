package com.example.kotlinstart.repository.detailsrepository.datasource

import com.example.kotlinstart.BuildConfig
import com.example.kotlinstart.KotlinStartApplication.Companion.getWeatherAPI
import com.example.kotlinstart.dto.WeatherDTO
import retrofit2.Callback

class RemoteDataSource {

    /* private val weatherAPI = Retrofit.Builder()
         .baseUrl("https://api.weather.yandex.ru/")
         .addConverterFactory(
             GsonConverterFactory.create(
                 GsonBuilder().setLenient().create()
             )
         )
         .client(createOkHttpClient(PODInterceptor()))
         .build().create(WeatherAPI::class.java)*/

    fun getWeatherDetails(lat: Double, lon: Double, callback: Callback<WeatherDTO>) {
        getWeatherAPI().getWeather(BuildConfig.WEATHER_API_KEY, lat, lon).enqueue(callback)
        //weatherAPI.getWeather(BuildConfig.WEATHER_API_KEY, lat, lon).enqueue(callback)
    }
/*
    private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        return httpClient.build()
    }

    inner class PODInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            return chain.proceed(chain.request())
        }
    }*/
}