package com.guard.afx.ui.fragment

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import com.guard.afx.R
import com.guard.afx.base.BaseFragment
import com.guard.afx.databinding.FragmentHomeBinding
import com.guard.afx.viewmodel.HomeFragmentViewModel



class HomeFragment : BaseFragment<HomeFragmentViewModel,FragmentHomeBinding>() {
    override fun initView(savedInstanceState: Bundle?) {

        mViewBind.apply {
            btnWifi.setOnClickListener {
               if (getWlanStatus(requireActivity())&& isOpenGps()){
                   val bundle = Bundle()
                   bundle.putString("name", "wifi")
                   NavHostFragment.findNavController(this@HomeFragment).navigate(R.id.action_mainFragment_to_pinholeDetectionFragment,bundle)
               } else{
                   Toast.makeText(requireActivity(),"没有打开WiFi和GPS",Toast.LENGTH_LONG).show()
               }
        }

            ccBluetooth.setOnClickListener{
                if (isOpenBluetooth()){
                    val bundle = Bundle()
                    bundle.putString("name", "Bluetooth")
                    NavHostFragment.findNavController(this@HomeFragment).navigate(R.id.action_mainFragment_to_pinholeDetectionFragment,bundle)
                }else{
                    Toast.makeText(requireActivity(),"没有打开蓝牙",Toast.LENGTH_LONG).show()
                }

            }

            ccMagneticField.setOnClickListener{
                NavHostFragment.findNavController(this@HomeFragment).navigate(R.id.action_mainFragment_to_magneticFragment)
            }


        }

    }


    /**
     * 判断WiFi是否开启
     */
    private fun getWlanStatus(ctx: Context): Boolean {
        //从系统服务中获取无线网络管理器
        val wm = ctx.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wm.isWifiEnabled
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    private fun isOpenGps() : Boolean{
        val locationManager : LocationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gps : Boolean = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val network : Boolean = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (gps || network){
            return true
        }
        return false
    }

    /**
     * 判断蓝牙是否打开
     */
    private fun isOpenBluetooth() : Boolean{
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        Log.d("TAG", "蓝牙是否打开: " + bluetoothAdapter?.isEnabled)
        val bluetooth : Boolean? = bluetoothAdapter?.isEnabled
        return bluetooth!!
    }


}