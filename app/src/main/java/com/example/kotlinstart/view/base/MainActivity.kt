package com.example.kotlinstart.view.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinstart.R
import com.example.kotlinstart.view.mainscreen.MainFragment

//Последние работы по этому приложению ДЗ:

// разобраться с удалением элементво из бд +

// проверкой на дубли +

// доработать функционал по onBackPress() выводить тост с уведомлением для выхода +



internal class MainActivity : AppCompatActivity() {

    private var isExit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        openMainFragment()
    }

    private fun openMainFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, MainFragment())
            .commitAllowingStateLoss()
    }

    override fun onBackPressed() {
        when (supportFragmentManager.backStackEntryCount) {
            0 -> checkExit()
            else -> {
                super.onBackPressed()
                isExit = false
            }
        }
    }

    private fun checkExit() {
        when (isExit) {
            true -> {
                super.onBackPressed()
            }
            else -> {
                Toast.makeText(this, R.string.exit, Toast.LENGTH_SHORT).show()
                isExit = true
            }
        }
    }
}
