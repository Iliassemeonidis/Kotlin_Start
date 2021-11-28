package com.example.kotlinstart.view.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinstart.R
import com.example.kotlinstart.view.mainscreen.MainFragment

//По ДЗ:

// java.lang.NullPointerException: addresses[0].locality must not be null +

// отрабоать 4 сченария по обработки навигации между фрагментами

// разобраться с onBackPressed()

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

//    override fun onBackPressed() {
//        when (supportFragmentManager.backStackEntryCount) {
//            0 -> checkExit()
//            else -> {
//                supportFragmentManager.popBackStack()
//                isExit = false
//            }
//        }
//    }

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
