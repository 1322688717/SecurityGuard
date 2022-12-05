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
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.os.postDelayed
import androidx.navigation.fragment.NavHostFragment
import com.guard.afx.base.BaseFragment
import com.guard.afx.bluetooth.*
import com.guard.afx.databinding.FragmentPinholeDetectionBinding
import com.guard.afx.viewmodel.BlueToothViewMode
import com.guard.afx.viewmodel.PinholeDetectionViewModel
import com.permissionx.guolindev.PermissionX


class PinholeDetectionFragment : BaseFragment<PinholeDetectionViewModel, FragmentPinholeDetectionBinding>(),
    Runnable {

    private lateinit var mList: List<ScanResult>
    //进度条进度
    var p = 0
     var handler: Handler = Handler()
     private var runnable: Runnable = Runnable(){
         handler.postDelayed(this,1)
     }


    var name : String = ""
    //上下文
    private var mContext = this

    private var mBluetoothFilter: IntentFilter? = null
    //蓝牙广播
    private var mBluetoothReceiver: BluetoothReceiver? = null
    //蓝牙viewmodel
    var blueToothViewMode: BlueToothViewMode? = null





    @RequiresApi(Build.VERSION_CODES.M)
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

    @SuppressLint("MissingPermission")
    private fun getBluetooth() {
        val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter.isEnabled) { //打开
            //开始扫描周围的蓝牙设备,如果扫描到蓝牙设备，通过广播接收器发送广播
            bluetoothAdapter.startDiscovery()
        } else {
            Toast.makeText(activity,"请打开蓝牙",Toast.LENGTH_LONG).show()
        }
        getBlueTooth()
    }

    fun getBlueTooth(){
        // 输出蓝牙列表
        if (!checkBluetoothEnable()) {
            // 动态注册
            if (mBluetoothReceiver == null) {
                mBluetoothReceiver = BluetoothReceiver(blueToothViewMode)
            }
            if (mBluetoothFilter == null) {
                mBluetoothFilter = BluetoothReceiver.registerIntentFilter()
            }
            if (mBluetoothReceiver != null && mBluetoothFilter != null) {
                activity?.registerReceiver(mBluetoothReceiver, mBluetoothFilter)
            }
        }
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


    /**
     * 请求权限回调
     */
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


    /**
     * 设置进度条
     */
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
            handler.removeCallbacks(runnable)
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
        if (mBluetoothReceiver != null){
            requireActivity().unregisterReceiver(mBluetoothReceiver)
        }

    }




}