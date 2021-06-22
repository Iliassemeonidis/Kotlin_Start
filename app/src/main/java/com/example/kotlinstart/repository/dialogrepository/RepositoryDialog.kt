package com.example.kotlinstart.repository.dialogrepository

import com.example.kotlinstart.model.CityData

interface RepositoryDialog {

    fun getListCityNamesFromLocalStorage():ArrayList<CityData>
}
