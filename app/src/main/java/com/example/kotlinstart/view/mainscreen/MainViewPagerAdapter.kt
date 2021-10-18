package com.example.kotlinstart.view.mainscreen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainViewPagerAdapter(
    activity: FragmentActivity,
    private val listDetailsFragment: ArrayList<DetailsFragment>
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return listDetailsFragment.size
    }

    override fun createFragment(position: Int): Fragment {
     return listDetailsFragment[position]
    }
}