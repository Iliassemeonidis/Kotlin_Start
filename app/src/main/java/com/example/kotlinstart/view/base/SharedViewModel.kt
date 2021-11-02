package com.example.kotlinstart.view.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

internal class SharedViewModel(
    private val liveDataForObservation: MutableLiveData<String> = MutableLiveData()
) : ViewModel() {

   private var city:String = ""

    fun subscribe(): LiveData<String> {
        return liveDataForObservation
    }

    fun setCity(city: String) {
        this.city = city
        liveDataForObservation.value = city
    }
}