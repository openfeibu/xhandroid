package cn.flyexp.permission;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.util.SparseArray;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.flyexp.R;

public class PermissionTools {

    public static void requestPermission(final Context activity,  PermissionHandler.PermissionCallback permissionCallback, int[] ids){
        PermissionHandler.createBuilder(activity).setPermission(ids).
                setNegativeString(activity.getString(R.string.permission_dlg_negative)).
                setCallBack(permissionCallback).request();
    }

}