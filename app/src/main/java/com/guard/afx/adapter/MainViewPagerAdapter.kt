package com.guard.afx.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainViewPagerAdapter(framentActivity: Fragment, val fragments: ArrayList<Fragment>) :
    FragmentStateAdapter(framentActivity) {
    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment =
        fragments[position]
}