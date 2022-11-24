package com.guard.afx.ui.fragment

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.guard.afx.R
import com.guard.afx.base.BaseFragment
import com.guard.afx.databinding.FragmentHomeBinding
import com.guard.afx.viewmodel.HomeFragmentViewModel


class HomeFragment : BaseFragment<HomeFragmentViewModel,FragmentHomeBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

        mViewBind.apply {
            btnWifi.setOnClickListener {
            //传值fragment
            val bundle = Bundle()
            bundle.putString("name", "wifi")
            NavHostFragment.findNavController(this@HomeFragment).navigate(R.id.action_mainFragment_to_pinholeDetectionFragment,bundle)
        }

            ccBluetooth.setOnClickListener{
                val bundle = Bundle()
                bundle.putString("name", "Bluetooth")
                NavHostFragment.findNavController(this@HomeFragment).navigate(R.id.action_mainFragment_to_pinholeDetectionFragment,bundle)
            }

            ccMagneticField.setOnClickListener{
                NavHostFragment.findNavController(this@HomeFragment).navigate(R.id.action_mainFragment_to_magneticFragment)
            }


        }

    }


}