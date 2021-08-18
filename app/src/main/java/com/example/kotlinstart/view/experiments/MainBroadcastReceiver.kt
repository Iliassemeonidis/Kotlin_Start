package com.example.kotlinstart.view.experiments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.net.ConnectivityManager.EXTRA_NO_CONNECTIVITY
import android.widget.Toast

class MainBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val extra = intent?.getBooleanExtra(EXTRA_NO_CONNECTIVITY, false)
        if (extra != null) {
            if (extra) {
                StringBuilder().apply {
                    append("СООБЩЕНИЕ ОТ СИСТЕМЫ\n")
                    append("Action: ${intent.action}")
                    toString().also {
                        Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}
