package com.projects.aldajo92.jetsonbotunal.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.projects.aldajo92.jetsonbotunal.NUM_PAGES
import com.projects.aldajo92.jetsonbotunal.ui.data.DataListFragment
import com.projects.aldajo92.jetsonbotunal.ui.graphs.GraphsFragment

class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment = when(position){
        0 -> GraphsFragment()
        else -> DataListFragment()
    }
}
