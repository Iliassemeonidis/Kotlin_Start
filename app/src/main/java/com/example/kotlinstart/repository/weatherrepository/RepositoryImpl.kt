package com.example.kotlinstart.repository.weatherrepository

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import com.example.kotlinstart.KotlinStartApplication
import com.example.kotlinstart.model.Weather
import com.example.kotlinstart.model.WeatherParams
import com.example.kotlinstart.room.HistoryEntity
import com.example.kotlinstart.view.base.baseinterface.OnGetAddressListener
import com.example.kotlinstart.view.base.baseinterface.OnGetWeatherFragmentList
import com.example.kotlinstart.view.base.baseinterface.OnGetWeatherListListener
import com.example.kotlinstart.view.detailsscreen.DetailsFragment

class RepositoryImpl : Repository {

    override fun getAddress(context: Context, address: String, listener: OnGetAddressListener) {
        KotlinStartApplication.getGeolocationHelper().getAddressAsyncByCity(
            context,
            address,
            listener
        )
    }

    override fun saveCityInDataBase(city: WeatherParams) {
        val handler = Handler(createThread().looper)
        handler.post {
            KotlinStartApplication.getHistoryDao().insert(
                HistoryEntity(
                    0,
                    city.city,
                    city.degrees,
                    city.weatherCondition
                )
            )
        }
    }

    override fun getWeatherParamsFromDataBase(listener: OnGetWeatherFragmentList) {
        val handler = Handler(createThread().looper)
        handler.post {
            val listDao = KotlinStartApplication.getHistoryDao().all()
            val list = mutableListOf<DetailsFragment>()
            for (i in listDao.indices) {
                list.add(
                    i,
                    DetailsFragment.newInstance(
                        WeatherParams(
                            listDao[i].city,
                            listDao[i].temperature,
                            listDao[i].condition
                        )
                    )
                )
            }
            listener.onListFragment(list)
        }
    }

    override fun getWeatherFromDataBase(listener: OnGetWeatherListListener) {
        val handler = Handler(createThread().looper)
        handler.post {
            val listDao = KotlinStartApplication.getHistoryDao().all()

            val list = mutableSetOf<Weather>()
            for (i in listDao.indices) {
                list.add(
                    Weather(
                        cityName = listDao[i].city, "",
                        listDao[i].temperature,
                    )
                )
            }
            val listWeather : MutableList<Weather> = list.toMutableList()
            listener.onListReady(listWeather)
        }
    }

    override fun deleteAllWeatherParamsFromDataBase() {
        val handler = Handler(createThread().looper)
        handler.post {
            val listDao = KotlinStartApplication.getHistoryDao().all()
            for (i in listDao.indices) {
                KotlinStartApplication.getHistoryDao().delete(listDao[i])
            }
        }
    }

    private fun createThread(): HandlerThread {
        val handlerThread = HandlerThread("MyThread2")
        handlerThread.start()
        return handlerThread
    }
}
