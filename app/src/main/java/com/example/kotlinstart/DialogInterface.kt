package com.example.kotlinstart

interface DialogInterface  {
    fun showDialogGeolocationIsClosed()
    fun showDialogGeolocationIsDisabled()
    fun showRationaleDialog()
    fun showAddressDialog(city: String)
    fun alertDialog()
}