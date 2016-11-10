package cn.flyexp.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by tanxinye on 2016/10/23.
 */
public class PackageUtil {

    public static boolean checkPhoneStatePermission(Context context) {
        return (PackageManager.PERMISSION_GRANTED == context.getPackageManager().checkPermission("android.permission.READ_PHONE_STATE", context.getPackageName()));
    }

    public static String getVersionName(Context context) {
        return getPackageInfo(context).versionName;
    }

    public static int getVersionCode(Context context) {
        return getPackageInfo(context).versionCode;
    }

    public static PackageInfo getPackageInfo(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
