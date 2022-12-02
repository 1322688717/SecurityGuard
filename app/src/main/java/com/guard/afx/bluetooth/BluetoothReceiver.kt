package com.guard.afx.bluetooth

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log


/**
 * @author：HLQ_Struggle
 * @date：2021/8/28
 * @desc：
 */
class BluetoothReceiver : BroadcastReceiver() {

    companion object {

        fun registerIntentFilter(): IntentFilter {
            val intentFilter = IntentFilter()
            intentFilter.apply {
                addAction(BluetoothAdapter.ACTION_STATE_CHANGED); // 蓝牙状态改变
                addAction("android.bluetooth.BluetoothAdapter.STATE_OFF"); // 本地蓝牙适配器已关闭
                addAction("android.bluetooth.BluetoothAdapter.STATE_ON"); // 本地蓝牙适配器已打开，可以使用
                addAction(BluetoothDevice.ACTION_ACL_CONNECTED); // 已和远程设备建立 ACL 连接
                addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED); // 与远程设备 ACL 断开连接
                priority = Int.MAX_VALUE
            }
            return intentFilter
        }

    }

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
                        Log.i("蓝牙已开启输出列表", "onReceive: ${fetchAlReadyConnection()}")
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
            else -> "action $action"
        }
        Log.e("HLQ", "----> bluetoothLog $bluetoothLog")
    }

}
