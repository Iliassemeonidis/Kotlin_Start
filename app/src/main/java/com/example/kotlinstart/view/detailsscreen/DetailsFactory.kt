package com.example.kotlinstart.view.detailsscreen

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DetailsFactory(
    private val application: Application= Application(),
    private val city: String
) :
    ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailsViewModel(application = application, city = city) as T
    }
}