package cn.flyexp.permission;

import android.content.Context;

import cn.flyexp.R;

public class PermissionTools {

    public static void requestPermission(final Context activity, PermissionHandler.PermissionCallback permissionCallback, int[] ids){
        PermissionHandler.createBuilder(activity).setPermission(ids).
                setNegativeString(activity.getString(R.string.permission_dlg_negative)).
                setCallBack(permissionCallback).request();
    }

}