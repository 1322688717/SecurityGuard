package com.guard.afx.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.MutableLiveData
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

class MainViewModel : BaseViewModel() {
    var isNetWork = MutableLiveData<Boolean>()

    init {
        isNetWork.value = false
    }

    fun SetNet(context: Context) {

        //判断上下文是不是空的
        //为啥要判断啊? 防止context是空的导致 报空指针异常
        //获取连接管理器
        val mConnectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //获取网络状态mConnectivityManager.getActiveNetworkInfo();
        val mNnetNetworkInfo = mConnectivityManager.activeNetworkInfo
        if (mNnetNetworkInfo != null) {
            //判断网络是否可用//如果可以用就是 true  不可用就是false
            this.isNetWork.value =  mNnetNetworkInfo.isAvailable
        }
    }

    fun GetNet() : MutableLiveData<Boolean> {
        return isNetWork
    }
}