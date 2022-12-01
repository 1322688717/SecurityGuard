package com.guard.afx.ui.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import com.guard.afx.base.BaseActivity
import com.guard.afx.databinding.ActivityMainBinding
import com.guard.afx.utlis.StatusBarUtil
import com.guard.afx.viewmodel.MainViewModel

class MainActivity : BaseActivity<MainViewModel,ActivityMainBinding>() {


    override fun initView(savedInstanceState: Bundle?) {
        StatusBarUtil.initStatusBarDark(this)
        mViewModel.SetNet(this)
        isCheckNetWork()
        initClick()
    }

    private fun initClick() {
        mViewBind.btnRefresh.setOnClickListener {
            mViewModel.SetNet(this)
            isCheckNetWork()
        }
    }

    private fun isCheckNetWork() {
        mViewModel.isNetWork.observe(this){
            if (it) {
                mViewBind.notNetwork.visibility = View.GONE
                mViewBind.hostFragment.visibility = View.VISIBLE
            }else{
                mViewBind.notNetwork.visibility = View.VISIBLE
                mViewBind.hostFragment.visibility = View.GONE
            }
        }
    }
}