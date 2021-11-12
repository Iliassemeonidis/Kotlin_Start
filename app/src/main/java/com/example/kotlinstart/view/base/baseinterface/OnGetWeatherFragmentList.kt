package com.example.kotlinstart.view.base.baseinterface

import com.example.kotlinstart.view.detailsscreen.DetailsFragment

interface OnGetWeatherFragmentList {
    fun onListFragment(list: MutableList<DetailsFragment>)
}