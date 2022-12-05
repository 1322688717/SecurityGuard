package com.guard.afx.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import com.guard.afx.viewmodel.BlueToothViewMode


/**
 * @author：HLQ_Struggle
 * @date：2021/8/28
 * @desc：
 */
class BluetoothReceiver(private val viewmode : BlueToothViewMode?) : BroadcastReceiver() {


    var nameAndMac : HashMap<String,String> = HashMap()

    private val TAG = "BluetoothReceiver"
    var s = "."
    var i = 0

    companion object {
        fun registerIntentFilter(): IntentFilter {
            val intentFilter = IntentFilter()
            intentFilter.apply {
                intentFilter.addAction(BluetoothDevice.ACTION_FOUND);//获得扫描结果
                intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//绑定状态变化
                intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);//开始扫描
                intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//扫描结束
                addAction("android.bluetooth.BluetoothAdapter.STATE_OFF") // 本地蓝牙适配器已关闭
                addAction("android.bluetooth.BluetoothAdapter.STATE_ON") // 本地蓝牙适配器已打开，可以使用
                addAction(BluetoothDevice.ACTION_ACL_CONNECTED) // 已和远程设备建立 ACL 连接
                addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED) // 与远程设备 ACL 断开连接
            }
            return intentFilter
        }

    }


    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        action ?: return
        val bluetoothLog = when (action) {
            BluetoothAdapter.ACTION_STATE_CHANGED -> { // 监听蓝牙状态
                when (val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)) {
                    BluetoothAdapter.STATE_TURNING_ON -> {
                        "STATE_TURNING_ON 蓝牙开启中"
                    }
                    BluetoothAdapter.STATE_ON -> {
                        "STATE_ON 蓝牙已开启"
                    }
                    BluetoothAdapter.STATE_CONNECTING -> {
                        "STATE_CONNECTING 蓝牙连接中"
                    }
                    BluetoothAdapter.STATE_CONNECTED -> {
                        "STATE_CONNECTED 蓝牙已连接"
                    }
                    BluetoothAdapter.STATE_DISCONNECTING -> {
                        "STATE_DISCONNECTING 蓝牙断开中"
                    }
                    BluetoothAdapter.STATE_DISCONNECTED -> {
                        "STATE_DISCONNECTED 蓝牙已断开"
                    }
                    BluetoothAdapter.STATE_TURNING_OFF -> {
                        "STATE_TURNING_OFF 蓝牙关闭中"
                    }
                    BluetoothAdapter.STATE_OFF -> {
                        "STATE_OFF 蓝牙关闭"
                    }
                    else -> "ACTION_STATE_CHANGED EXTRA_STATE $state"
                }
            }
            BluetoothDevice.ACTION_ACL_CONNECTED -> { // 蓝牙已连接
                Log.e("HLQ", "----> ACTION_ACL_CONNECTED 蓝牙已连接 ") // 蓝牙已打开 且 已连接
                Log.e("HLQ", "----> 蓝牙已打开且已连接")
                Log.e("HLQ", "----> 输出已配对成功蓝牙列表")
                Log.e("HLQ", "----> ${fetchAlReadyConnection()}")

                "----> 当前连接蓝牙名称：${getConnectedBtDevice()}"
            }
            BluetoothDevice.ACTION_ACL_DISCONNECTED -> { // 蓝牙已断开
                "ACTION_ACL_DISCONNECTED 蓝牙已断开"
            }

            BluetoothDevice.ACTION_FOUND -> { //found device
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                if (!device?.name.isNullOrEmpty()) {
                    // 得到设备对象
//                    deviceBeans.add(device.toString())

                    // hash map name为key address 为 vale
                    nameAndMac.put(device?.address.toString(),device?.name.toString())
                    viewmode?.upData(nameAndMac.toString())
                }

                if (i<=8){
                    s=s+"."
                    i++
                } else {
                    s="."
                    i=0
                }
                viewmode?.state?.postValue("正在扫描"+"\n"+s)
                "發現設備"
            }

            BluetoothAdapter.ACTION_DISCOVERY_FINISHED ->{
                // 加载完成
                viewmode?.state?.postValue("扫描結束")
                "扫描結束"
            }

            BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                //加载的图像
                viewmode?.state?.postValue("正在扫描")
                "正在扫描"
            }

            else -> "action $action"
        }
        Log.e("HLQ", "----> bluetoothLog $bluetoothLog")
    }


}
