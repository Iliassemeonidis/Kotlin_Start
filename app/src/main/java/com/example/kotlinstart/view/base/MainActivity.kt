package com.example.kotlinstart.view.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinstart.R
import com.example.kotlinstart.view.mainscreen.MainFragment

//По ДЗ:

// замени Set на List +

// Разобраться с падением при отработке onBackPress() +

// Отработать нажатие на элименты списка (открывать нужный элемент во всью пейджере) +

// Отображать корректные данные в лист фрагмент и в пайджере  +
// (переделал бд данные смегрировал)

// * разобраться с удалением элементво из бд
// * узнать как делать рефрешь фрагмента и тогда убрать добавление нового фрагмента в weatherListFragment



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
