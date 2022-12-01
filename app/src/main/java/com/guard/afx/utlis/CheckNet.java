package com.guard.afx.utlis;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 检查设备是否联网
 */

public class CheckNet {
    //注意工具类中使用静态方法
    public static boolean isNetworkConnected(Context context){
        //判断上下文是不是空的
        //为啥要判断啊? 防止context是空的导致 报空指针异常
        if (context!=null){
            //获取连接管理器
            ConnectivityManager mConnectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取网络状态mConnectivityManager.getActiveNetworkInfo();
            NetworkInfo mNnetNetworkInfo=mConnectivityManager.getActiveNetworkInfo();
            if (mNnetNetworkInfo!=null){
                //判断网络是否可用//如果可以用就是 true  不可用就是false
                return mNnetNetworkInfo.isAvailable();
            }
        }
        return false;
    }

}
