package com.guard.afx.ui.activity;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.widget.Toast;

import com.guard.afx.databinding.ActivityNewSplashBinding;
import com.guard.afx.base.BaseActivity;
import com.guard.afx.utlis.CheckNet;
import com.guard.afx.viewmodel.NewSplashViewModel;

public class NewSplashActivity extends BaseActivity<NewSplashViewModel, ActivityNewSplashBinding> {

    private boolean isNetWork;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {

    }


}