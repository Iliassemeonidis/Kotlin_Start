package com.example.kotlinstart.view.shared

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinstart.R
import com.example.kotlinstart.view.weatherscreen.WeatherFragment
import java.io.BufferedReader
import java.util.stream.Collectors

/*
по ДЗ:
-прочитать 4 методичку
-перенести запросы в Репозиторий
-прочитать 5 методичку и сделать свой браузер в Погодном приложении
* */

internal class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        /*ok.setOnClickListener {
            try {
                val uri = URL(url.text.toString())
                Thread {
                    var urlConnection: HttpsURLConnection? = null
                    try {
                        urlConnection = uri.openConnection() as HttpsURLConnection
                        urlConnection.requestMethod = "GET" // установка метода получения данных — GET
                        urlConnection.readTimeout = 10000 // установка таймаута — 10 000 миллисекунд
                        val reader = BufferedReader(InputStreamReader(urlConnection.inputStream)) // читаем  данные в поток
                        val result = getLines(reader)
                        runOnUiThread {
                            webview.loadDataWithBaseURL(null, result, "text/html; charset=utf-8",
                                "utf-8", null)
                        }
                    } catch (e: Exception) {
                        Log.e("", "Fail connection", e)
                        e.printStackTrace()
                    } finally {
                        urlConnection?.disconnect()
                    }
                }.start()
            } catch (e: MalformedURLException) {
                Log.e("", "Fail URI", e)
                e.printStackTrace()
            }
        }*/
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.list_container, WeatherFragment())
            .commitAllowingStateLoss()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    override fun onBackPressed() {
        when (supportFragmentManager.backStackEntryCount) {
            0 -> checkExit()
            else -> {
                supportFragmentManager.popBackStack()
                isExit = false
            }
        }
    }

    private fun checkExit() {
        when (isExit) {
            true -> super.onBackPressed()
            else -> {
                Toast.makeText(this, R.string.exit, Toast.LENGTH_SHORT).show()
                isExit = true
            }
        }
    }

    companion object {
        var isExit = false
    }
}
