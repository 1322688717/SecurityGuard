package com.guard.afx.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.ScanResult
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.navigation.fragment.NavHostFragment
import com.guard.afx.base.BaseFragment
import com.guard.afx.databinding.FragmentPinholeDetectionBinding
import com.guard.afx.viewmodel.PinholeDetectionViewModel
import me.hgj.jetpackmvvm.ext.util.TAG


class PinholeDetectionFragment : BaseFragment<PinholeDetectionViewModel, FragmentPinholeDetectionBinding>(),
    Runnable {

    private lateinit var mList: List<ScanResult>
    //进度条进度
    var p = 0
     var handler: Handler = Handler()
     private var runnable: Runnable = Runnable(){
         p++
         if (p==100){
             handler.removeCallbacks(this)
         }
         mViewBind.progressBar.progress = p
         handler.postDelayed(this,1)
     }

    lateinit var mWifiManager : WifiManager
    var name : String = ""
    private val receiver = object : BroadcastReceiver() {

        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            when(intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    // Discovery has found a device. Get the BluetoothDevice
                    // object and its info from the Intent.
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device?.name
                    val deviceHardwareAddress = device?.address // MAC address

                    Log.e("TAG","deviceName===$deviceName")
                    Log.e("TAG","deviceHardwareAddress===$deviceHardwareAddress")
                }
            }
        }
    }



    override fun initView(savedInstanceState: Bundle?) {

        val bundle = arguments
        name = bundle!!.getString("name").toString()
        if (name == "wifi"){
            mViewBind.tvTitle.text = "智能针孔检测"
            mViewBind.tvZero.text = "检测网络连接状态……"
            mViewBind.tvTwo.text = "正在检测当前无线网络中可能存在的针孔设备……"
            registerPermission()


        }else{
            mViewBind.tvTitle.text = "蓝牙检测"
            mViewBind.tvZero.text = "检测蓝牙连接状态……"
            mViewBind.tvTwo.text = "正在检测周围蓝牙中可能存在的针孔设备……"
            getBluetooth()
        }
        handler.postDelayed(runnable,1)
        initClick()

    }

    /**
     * 获取附件可连接蓝牙设备
     */
    private fun getBluetooth() {
        //获取 BluetoothAdapter
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
        Log.d("TAG", "蓝牙是否打开: " + bluetoothAdapter?.isEnabled)


        //启用蓝牙
        if (bluetoothAdapter?.isEnabled == false) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            startActivityForResult(enableBtIntent, 0)
        }

        //查找设备
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        requireActivity().registerReceiver(receiver, filter)


    }



    private fun initClick() {
        mViewBind.apply {
            imgBack.setOnClickListener {
                handler.removeCallbacks(this@PinholeDetectionFragment)
                NavHostFragment.findNavController(this@PinholeDetectionFragment).navigateUp()
            }
        }
    }


    /**
     * 获取附近WiFi列表
     */
    private fun getWifiList(): List<ScanResult> {
        val wifiManager: WifiManager = requireContext().applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val scanWifiList: ArrayList<ScanResult> = wifiManager.scanResults as ArrayList<ScanResult>
        Log.i("TAG", "getWifiList: ${wifiManager.connectionInfo}")
        val wifiList = ArrayList<ScanResult>()
        if (scanWifiList.isNotEmpty()) {
            wifiList.addAll(scanWifiList)
        } else {
            Log.e("TAG", "没有获取到Wifi列表")
        }
        return wifiList

    }


    /**
     * 请求权限
     */
    private fun registerPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            checkSelfPermission(requireContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION), 100)
        } else {
            mList = getWifiList()
        }
    }




    @SuppressLint("SetTextI18n")
    override fun run() {
        p++
        if (p==40){
            mViewBind.tvOne.visibility = View.VISIBLE
        }
        if (p==80){
            mViewBind.tvTwo.visibility = View.VISIBLE
        }
        if (p==100){
            handler.removeCallbacks(this)
            mViewBind.ccRead.visibility = View.GONE
            mViewBind.llFinsh.visibility = View.VISIBLE
        }
        mViewBind.tvRatio.text = "$p%"
        mViewBind.progressBar.progress = p
        handler.postDelayed(this,50)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e("TAG","onDestroy")
        handler.removeCallbacks(this@PinholeDetectionFragment)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            mList = getWifiList()
        }
    }



}