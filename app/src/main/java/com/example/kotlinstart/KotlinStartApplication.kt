package com.example.kotlinstart

import android.app.Application
import androidx.room.Room
import com.example.kotlinstart.repository.detailsrepository.datasource.WeatherAPI
import com.example.kotlinstart.room.HistoryDao
import com.example.kotlinstart.room.HistoryDataBase
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class KotlinStartApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {

        private var appInstance: KotlinStartApplication? = null
        private var db: HistoryDataBase? = null
        private const val DB_NAME = "History.db"
        private var retrofit: Retrofit? = null

        fun getHistoryDao(): HistoryDao {
            if (db == null) {
                synchronized(HistoryDataBase::class.java) {
                    if (db == null) {
                        if (appInstance == null) throw IllegalStateException("Application is null while creating DataBase")
                        db = Room.databaseBuilder(
                            appInstance!!.applicationContext,
                            HistoryDataBase::class.java,
                            DB_NAME
                        )
                            .build()
                    }
                }
            }
            return db!!.historyDao()
        }

        fun getWeatherAPI(): WeatherAPI {
            if (retrofit == null) {
                synchronized(Retrofit::class.java) {
                    if (retrofit == null) {
                        if (appInstance == null) throw IllegalStateException("Application is null while creating Retrofit")
                        retrofit = Retrofit.Builder()
                            .baseUrl("https://api.weather.yandex.ru/")
                            .addConverterFactory(
                                GsonConverterFactory.create(
                                    GsonBuilder().setLenient().create()
                                )
                            )
                            .client(createOkHttpClient(PODInterceptor()))
                            .build()
                    }
                }
            }
            return retrofit!!.create(WeatherAPI::class.java)
        }

        private fun createOkHttpClient(interceptor: Interceptor): OkHttpClient {
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(interceptor)
            httpClient.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            return httpClient.build()
        }

        private class PODInterceptor : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                return chain.proceed(chain.request())
            }
        }
    }
}