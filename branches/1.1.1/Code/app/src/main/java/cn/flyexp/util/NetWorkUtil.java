package cn.flyexp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import cn.flyexp.MainActivity;

/**
 * Created by tanxinye on 2016/10/23.
 */
public class NetWorkUtil {

    public static Context getContext() {
        return MainActivity.getActivity();
    }

    public static boolean isNetWorkAvailable() {
        NetworkInfo networkInfo = getNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static boolean isWifiConnected() {
        NetworkInfo networkInfo = getNetworkInfo();
        return networkInfo != null && isNetWorkAvailable() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    private static NetworkInfo getNetworkInfo() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }
}
