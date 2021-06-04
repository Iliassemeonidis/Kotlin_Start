package com.example.kotlinstart.utils

import java.text.SimpleDateFormat
import java.util.*

fun Date.myFormat(): String {
    return SimpleDateFormat("yyyy.dd.MMM mm:HH", Locale.getDefault()).format(this)
}
