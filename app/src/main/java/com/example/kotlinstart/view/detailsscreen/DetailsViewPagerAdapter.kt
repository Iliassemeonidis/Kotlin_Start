package com.example.kotlinstart.view.detailsscreen

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class DetailsViewPagerAdapter(
    activity: FragmentActivity,
    private var listDetailsFragment: MutableList<DetailsFragment>
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return listDetailsFragment.size
    }

    override fun createFragment(position: Int): Fragment {
     return listDetailsFragment[position]
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addNewList(list: MutableList<DetailsFragment>) {
        listDetailsFragment = list
        notifyDataSetChanged()
    }
}