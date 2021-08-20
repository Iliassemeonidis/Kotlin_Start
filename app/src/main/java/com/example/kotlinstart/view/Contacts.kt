package com.example.kotlinstart.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.kotlinstart.R

const val REQUEST_CODE = 42

class Contacts : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)
        checkPermission()
    }

    private fun checkPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) ==
                    PackageManager.PERMISSION_GRANTED -> {
                //Доступ к контактам на телефоне есть
                //getContacts()

                Toast.makeText(this, "Контакты", Toast.LENGTH_SHORT).show()
            }
            //Опционально: если нужно пояснение перед запросом разрешений
            shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS) -> {
                AlertDialog.Builder(this)
                    .setTitle("Доступ к контактам")
                    .setMessage("Объяснение")
                    .setPositiveButton("Предоставить доступ") { _, _ ->
                        requestPermission()
                    }
                    .setNegativeButton("Не надо") { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
            }
            else -> {
                //Запрашиваем разрешение
                requestPermission()
            }
        }
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS), REQUEST_CODE)
    }

    // Обратный вызов после получения разрешений от пользователя
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                // Проверяем, дано ли пользователем разрешение по нашему запросу
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    Toast.makeText(this, "Контакты", Toast.LENGTH_SHORT).show()
                    //getContacts()
                } else {
                    // Поясните пользователю, что экран останется пустым, потому что доступ к контактам не предоставлен
                    AlertDialog.Builder(this)
                        .setTitle("Доступ к контактам")
                        .setMessage("Объяснение")
                        .setNegativeButton("Закрыть") { dialog, _ -> dialog.dismiss() }
                        .create()
                        .show()
                }

                return
            }
        }
    }

}