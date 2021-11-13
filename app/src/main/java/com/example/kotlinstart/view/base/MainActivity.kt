package com.example.kotlinstart.view.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinstart.R
import com.example.kotlinstart.view.mainscreen.MainFragment

//По ДЗ:

// Отработать нажатие на элименты списка
// Решить проблему с добавлением нового элемента в WeatherListFragment

//- Доделать остальные TODO +(за исключением некоторых не методов)

//*разобраться с удалением элементво из бд

// Разобраться с действием назад после добавления города т.е решить две проблемы :
// 1) После добавления новго города в список и нажатя назад приложение падает +
// 2) Обновление +

//Добавить еще один уровень вложенности , то есть сделать отдельный фрагмент дли детализации +
//(сделал из main_activity.xml контейнер для фрагментов)

//Обработка onBackPressed для поддержки FAB +
// (благодаря использованию main_activity. как контейнера для фрагментов решил эту проблему)

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
                supportFragmentManager.popBackStack()
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
