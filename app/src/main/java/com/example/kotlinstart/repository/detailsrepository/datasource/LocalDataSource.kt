package com.example.kotlinstart.repository.detailsrepository.datasource

import com.example.kotlinstart.KotlinStartApplication.Companion.getHistoryDao

class LocalDataSource {

    /*fun getWeatherHistory(liveDataForObservation) {
        Thread {
            liveDataForObservation.post(getHistoryDao().all())
        }.start()
    }*/

    fun getWeatherHistory() = getHistoryDao().all()

}
