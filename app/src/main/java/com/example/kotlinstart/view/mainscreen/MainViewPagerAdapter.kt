package com.example.kotlinstart.view.mainscreen

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainViewPagerAdapter(
    activity: FragmentActivity,
    private var listDetailsFragment: MutableList<MainFragment>
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return listDetailsFragment.size
    }

    override fun createFragment(position: Int): Fragment {
     return listDetailsFragment[position]
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addNewList(list: MutableList<MainFragment>) {
        listDetailsFragment = list
        notifyDataSetChanged()
    }

    fun addNewItem(item:MainFragment) {
        listDetailsFragment.add(item)
        if (listDetailsFragment.size == 0) {
            notifyItemChanged(0)
        } else {
            notifyItemChanged(listDetailsFragment.size-1)
        }
    }
}