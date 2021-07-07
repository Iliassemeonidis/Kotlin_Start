package com.example.kotlinstart.repository.dialogrepository

import com.example.kotlinstart.model.getListOfCityNames

internal class RepositoryDialogImpl : RepositoryDialog {

    override fun getListCityNamesFromLocalStorage() = getListOfCityNames()
}
