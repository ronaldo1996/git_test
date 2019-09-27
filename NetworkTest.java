package com.example.myapplication111;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkTest {



    //直接判断网络是不是可用，数据，WIFI都包括
    public boolean isNetworkAvailable(Context context){
        ConnectivityManager manager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=manager.getActiveNetworkInfo();
        //需要权限ACCESS_NETWORK_STATE
        if (networkInfo!=null){
            return true;
        }else {
            return false;
        }
    }


    //单独判断WiFi，如果时数据就换成TYPE_MOBILE
    public boolean isWifiAvailable(Context context){
        ConnectivityManager manager=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo!=null){
            return true;
        }else {
            return false;
        }
    }
}
