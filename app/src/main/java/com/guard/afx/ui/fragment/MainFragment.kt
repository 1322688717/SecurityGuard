package com.guard.afx.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.guard.afx.R
import com.guard.afx.adapter.MainViewPagerAdapter

import com.guard.afx.base.BaseFragment
import com.guard.afx.databinding.FragmentMainBinding
import com.guard.afx.viewmodel.MainFragmentViewModel


class MainFragment : BaseFragment<MainFragmentViewModel,FragmentMainBinding>() {

    var fragments : ArrayList<Fragment> = ArrayList<Fragment>()

    override fun initView(savedInstanceState: Bundle?) {
        mViewBind.apply {

            fragments.add(HomeFragment())
            fragments.add(MineFragment())


            mainViewpager.adapter = MainViewPagerAdapter(this@MainFragment,fragments)

            mainBottom.setOnNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.menu_home -> mainViewpager.currentItem = 0

                    R.id.menu_mine -> mainViewpager.currentItem = 1
                    else -> {
                        return@setOnNavigationItemSelectedListener false
                    }
                }
                false
            }

            //禁止用户滑动
            mainViewpager.isUserInputEnabled = false

            //实现viewpager和用户联动
//            mainViewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//                override fun onPageSelected(position: Int) {
//                    super.onPageSelected(position)
//                    mainBottom.menu.getItem(position).isChecked = true
//                }
//            })
        }
    }

}