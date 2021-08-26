package com.example.kotlinstart.view

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.example.kotlinstart.R
import kotlinx.android.synthetic.main.activity_contacts.*

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
                getContacts()
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
                            //если запросов на резрешение не один то нужно запускать цыкл для поиска нужного
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    Toast.makeText(this, "Контакты", Toast.LENGTH_SHORT).show()
                    //getContacts()
                } else {
                    // Поясните пользователю, что экран останется пустым,
                    // потому что доступ к контактам не предоставлен
                    AlertDialog.Builder(this)
                        .setTitle("Доступ к контактам")
                        .setMessage("Просто открой доступ к контактам и не тупи")
                        .setNegativeButton("Закрыть") { dialog, _ -> dialog.dismiss() }
                        .create()
                        .show()
                }
                return
            }
        }
    }

    private fun getContacts() {
        this.let {
            // Получаем ContentResolver у контекста
            val contentResolver: ContentResolver = it.contentResolver
            // Отправляем запрос на получение контактов и получаем ответ в виде Cursor
            val cursorWithContacts: Cursor? = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
            )

            cursorWithContacts?.let { cursor ->
                for (i in 0..cursor.count) {
                    // Переходим на позицию в Cursor
                    if (cursor.moveToPosition(i)) {
                    // Берём из Cursor столбец с именем
                        val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                        // подумать как достать номер телефона
                        val number = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY))

                        addView(it, name/*,number*/)
                    }
                }
            }
            cursorWithContacts?.close()
        }
    }

    private fun addView(context: Context, textToShow: String/*, number : Int*/) {
        containerForContacts.addView(AppCompatTextView(context).apply {
            text = textToShow
            textSize = resources.getDimension(R.dimen.main_container_text_size)
        })
     /*   containerForContacts.addView(AppCompatTextView(context).apply {
            text = number.toString()
            textSize = resources.getDimension(R.dimen.main_container_text_size)
        })*/
    }

}