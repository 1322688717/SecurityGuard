package com.guard.afx.ui.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.test.core.app.ApplicationProvider
import com.guard.afx.R
import com.guard.afx.base.BaseFragment
import com.guard.afx.databinding.FragmentHomeBinding
import com.guard.afx.viewmodel.HomeFragmentViewModel
import me.hgj.jetpackmvvm.ext.util.wifiManager


class HomeFragment : BaseFragment<HomeFragmentViewModel,FragmentHomeBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

        mViewBind.apply {
            btnWifi.setOnClickListener {
               if (getWlanStatus(requireActivity())){
                   val bundle = Bundle()
                   bundle.putString("name", "wifi")
                   NavHostFragment.findNavController(this@HomeFragment).navigate(R.id.action_mainFragment_to_pinholeDetectionFragment,bundle)
               } else{
                   Toast.makeText(requireActivity(),"没有打开WiFi",Toast.LENGTH_LONG).show()
               }
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



    fun getWlanStatus(ctx: Context): Boolean {
        //从系统服务中获取无线网络管理器
        val wm = ctx.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wm.isWifiEnabled
    }


}