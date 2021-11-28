package com.example.kotlinstart.view.weatherlistscreen

internal sealed class ListState {
    data class NotChanged(val refresh: Boolean) : ListState()
    data class ToPosition(val position: Int, val refresh: Boolean) : ListState()
}
