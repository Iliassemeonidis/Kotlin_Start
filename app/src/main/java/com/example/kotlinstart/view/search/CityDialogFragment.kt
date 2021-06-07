package com.example.kotlinstart.view.search

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.kotlinstart.R

internal class CityDialogFragment : DialogFragment() {

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            //todo допилить реализацию
            builder.setView(inflater.inflate(R.layout.fragment_city_dialog, null))
                .setPositiveButton("Apply") { _, _ -> println() }
                .setNegativeButton("Cancel") { _, _ -> println() }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}