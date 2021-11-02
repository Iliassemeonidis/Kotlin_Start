package com.example.kotlinstart.view.base.baseinterface

import com.example.kotlinstart.view.mainscreen.MainFragment

interface OnGetWeatherFragmentList {
    fun onListFragment(list: MutableList<MainFragment>)
}