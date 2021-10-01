package com.example.kotlinstart.view.detailsscreen

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class DetailsViewPagerAdapter(
    fragmentManager: FragmentActivity,
    private val listDetailsFragment: ArrayList<DetailsFragment>
) : FragmentStateAdapter(fragmentManager) {

    override fun getItemCount(): Int {
        return listDetailsFragment.size
    }

    override fun createFragment(position: Int): Fragment {
     return listDetailsFragment[position]
    }
}