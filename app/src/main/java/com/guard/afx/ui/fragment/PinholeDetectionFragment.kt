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
import androidx.navigation.fragment.NavHostFragment
import com.guard.afx.base.BaseFragment
import com.guard.afx.bluetooth.*
import com.guard.afx.databinding.FragmentPinholeDetectionBinding
import com.guard.afx.viewmodel.PinholeDetectionViewModel
import com.permissionx.guolindev.PermissionX


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


    var name : String = ""
    private val listBluetoothDevice: List<BluetoothDevice> = java.util.ArrayList()
    private var mContext = this

    private var mBluetoothFilter: IntentFilter? = null
    private var mBluetoothReceiver: BluetoothReceiver? = null





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

        val btAdapt = BluetoothAdapter.getDefaultAdapter()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PermissionX.init(this)
                .permissions(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
                )
                .request { allGranted, grantedList, deniedList ->
                    if (allGranted) {
                        Toast.makeText(
                            requireActivity(),
                            "All permissions are granted$grantedList",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        if (!btAdapt.isDiscovering) {
                            btAdapt.startDiscovery()
                        }
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "These permissions are denied:$deniedList",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

        btAdapt.startDiscovery()
        val intent = IntentFilter()
        intent.apply {
            addAction(BluetoothDevice.ACTION_FOUND)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
            addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            priority = IntentFilter.SYSTEM_HIGH_PRIORITY
        }
        mContext.requireActivity().registerReceiver(searchDevices, intent)



//        val resquestList = ArrayList<String>()
//        //新版本 Android12中
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            resquestList.add(Manifest.permission.BLUETOOTH_ADVERTISE)
//            resquestList.add(Manifest.permission.BLUETOOTH_SCAN)
//            resquestList.add(Manifest.permission.BLUETOOTH_CONNECT)
//        }
//
//
//        //安卓12以下
//        resquestList.add(Manifest.permission.BLUETOOTH)
//        resquestList.add(Manifest.permission.BLUETOOTH_ADMIN)
//
//        PermissionX.init(this)
//            .permissions(resquestList)
//            .onExplainRequestReason { scope, deniedList ->
//                scope.showRequestReasonDialog(deniedList, "需要这些权限", "OK", "Cancel")
//            }
//            .onForwardToSettings { scope, deniedList ->
//                scope.showForwardToSettingsDialog(deniedList, "你需要在设置中手动赋予权限", "OK", "Cancel")
//            }
//            .request { allGranted, grantedList, deniedList ->
//                if (allGranted) {
//                    Toast.makeText(requireActivity(), "所有权限都以获取", Toast.LENGTH_LONG).show()
//                } else {
//                    Toast.makeText(requireActivity(), "以下权限已被拒绝: $deniedList", Toast.LENGTH_LONG).show()
//                    fetchAlReadyConnection()
//                }
//            }
//
//        // 输出蓝牙列表
//        if (!checkBluetoothEnable()) {
//            if (mBluetoothReceiver == null) {
//                mBluetoothReceiver = BluetoothReceiver()
//            }
//            if (mBluetoothFilter == null) {
//                mBluetoothFilter = BluetoothReceiver.registerIntentFilter()
//            }
//            if (mBluetoothReceiver != null && mBluetoothFilter != null) {
//                activity?.registerReceiver(mBluetoothReceiver, mBluetoothFilter)
//            }
//
//            if (checkBluetoothStateEnable() && hasBluetoothAudioDevice()) { // 蓝牙已打开 且 已连接
//                Log.e("HLQ", "----> 蓝牙已打开且已连接")
//                Log.e("HLQ", "----> 输出已配对成功蓝牙列表")
//                Log.e("HLQ", "----> ${fetchAlReadyConnection()}")
//                Log.e("HLQ", "----> 当前连接蓝牙名称：${getConnectedBtDevice()}")
//            } else if (checkBluetoothStateEnable() && !hasBluetoothAudioDevice() ) {
//                Log.i("打印列表","${fetchAlReadyConnection()}")
//            }
//
//        }
    }


    private val searchDevices: BroadcastReceiver = object : BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    //  LogUtil.LOGE("ACTION_STATE_CHANGED")
                    Log.e("TAG","ACTION_STATE_CHANGED")
                }
                BluetoothDevice.ACTION_FOUND -> { //found device
                    val device =
                        intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)

                    if (!device?.name.isNullOrEmpty()) {
                        // 得到设备对象
                        Log.e("TAG","device===$device")

                        //mData.add(device)
                        //adapter.notifyDataSetChanged()
                    }
                }
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    // ToastUtil.show("正在扫描")
                    Log.e("TAG","正在扫描")
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    //ToastUtil.show("扫描完成，点击列表中的设备来尝试连接")
                    Log.e("TAG","扫描完成，点击列表中的设备来尝试连接")
                }
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




}