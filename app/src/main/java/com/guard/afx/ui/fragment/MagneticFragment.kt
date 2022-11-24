package com.guard.afx.ui.fragment

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.guard.afx.base.BaseFragment
import com.guard.afx.databinding.FragmentMagneticBinding
import com.guard.afx.viewmodel.MagneticFragmentViewModel

class MagneticFragment : BaseFragment<MagneticFragmentViewModel, FragmentMagneticBinding>(){
    override fun initView(savedInstanceState: Bundle?) {
        onclick()
    }

    private fun onclick() {
        mViewBind.apply {
            imgBack.setOnClickListener{
                NavHostFragment.findNavController(this@MagneticFragment).navigateUp()
            }
        }

    }

}