package com.example.kotlinstart.view.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

internal class SharedViewModel(
    private val liveDataForObservation: MutableLiveData<String> = MutableLiveData()
) : ViewModel() {

    fun subscribe(): LiveData<String> {
        return liveDataForObservation
    }

    fun setCity(city: String) {
        liveDataForObservation.value = city
    }
}
