package com.example.kotlinstart.repository.dialogrepository

import com.example.kotlinstart.model.getListOfCityNames

internal class RepositoryImplDialog : RepositoryDialog {

    override fun getListCityNamesFromLocalStorage() = getListOfCityNames()
}
