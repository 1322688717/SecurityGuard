package com.guard.afx.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.guard.afx.R
import com.guard.afx.base.BaseActivity
import com.guard.afx.databinding.ActivityMainBinding
import com.guard.afx.utlis.StatusBarUtil
import com.guard.afx.viewmodel.MainViewModel

class MainActivity : BaseActivity<MainViewModel,ActivityMainBinding>() {


    override fun initView(savedInstanceState: Bundle?) {
        StatusBarUtil.initStatusBarDark(this)
    }
}