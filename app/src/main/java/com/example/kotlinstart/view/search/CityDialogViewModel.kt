package com.example.kotlinstart.view.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kotlinstart.model.CityData
import com.example.kotlinstart.repository.dialogrepository.RepositoryImplDialog

internal class CityDialogViewModel(
    private val liveDataForObservation: MutableLiveData<ArrayList<CityData>> = MutableLiveData(),
    private val repositoryImpl: RepositoryImplDialog = RepositoryImplDialog()
) : ViewModel() {

    fun subscribe(): LiveData<ArrayList<CityData>> {
        return liveDataForObservation
    }

    fun getCityNamesList() {
        createCityData()
    }

    private fun createCityData() {
        liveDataForObservation.value = repositoryImpl.getListCityNamesFromLocalStorage()
    }

}
