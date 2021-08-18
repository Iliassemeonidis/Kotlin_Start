package com.example.kotlinstart

import android.app.Application
import android.os.Handler
import android.os.HandlerThread
import androidx.room.Room
import com.example.kotlinstart.room.HistoryDao
import com.example.kotlinstart.room.HistoryDataBase

class KotlinStartApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {

        private var appInstance: KotlinStartApplication? = null
        private var db: HistoryDataBase? = null
        private var historyDao:HistoryDao?=null
        private const val DB_NAME = "History.db"
        private val handlerThread = HandlerThread("Thread1")


        fun getHistoryDao(): HistoryDao {
            if (db == null) {
                synchronized(HistoryDataBase::class.java) {
                    if (db == null) {
                        if (appInstance == null) throw IllegalStateException("Application is null while creating DataBase")
                        handlerThread.start()
                        val handler = Handler(handlerThread.looper)
                        Thread {
                            handler.post {
                                db = Room.databaseBuilder(
                                    appInstance!!.applicationContext,
                                    HistoryDataBase::class.java,
                                    DB_NAME
                                )
//                                    .allowMainThreadQueries()
                                    .build()
                               historyDao = db!!.historyDao()
                            }
                        }.start()
                    }
                }
            }
            return db!!.historyDao()
        }

    }



}