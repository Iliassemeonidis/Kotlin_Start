package com.example.kotlinstart.view.search

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.kotlinstart.R
import com.example.kotlinstart.view.shared.SharedViewModel

internal class CityDialogFragment : DialogFragment() {
    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        //todo допилить реализацию
        builder.setView(inflater.inflate(R.layout.fragment_city_dialog, null))
            .setPositiveButton("Apply") { _, _ ->
                val sharedViewModel: SharedViewModel by activityViewModels()
                sharedViewModel.setCity("Moscow")
            }
            .setNegativeButton("Cancel") { _, _ -> println() }
        return builder.create()
    }
}
