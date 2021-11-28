package com.example.kotlinstart.view.weatherlistscreen

internal sealed class ListState {
   data class Old(val state:Boolean) : ListState()
   data class New(val state: Boolean) : ListState()
   data class MOVINGTOTHETOOLD(val position:Int) : ListState()
   data class MOVINGTOTHENEW(val position:Int) : ListState()
}