package cn.flyexp.permission;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.Settings;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.flyexp.R;

/**
 * Created by zlk on 2016/4/17.
 */
public class PermissionHandler {
    public static final int PERMISSION_PHONE = R.string.permission_dlg_phone;
    public static final int PERMISSION_FILE = R.string.permission_dlg_file;
    public static final int PERMISSION_CAMERA = R.string.permission_dlg_camera;
    private static final String SPNAME = "per";
    private static SparseArray<String> permissions = new SparseArray<String>(3);
    private static Map<String, Integer> permissions_pair = new HashMap<String, Integer>(3);

    static {
        permissions.put(PERMISSION_PHONE, "android.permission.READ_PHONE_STATE");
        permissions_pair.put("android.permission.READ_PHONE_STATE", PERMISSION_PHONE);
        permissions.put(PERMISSION_FILE, "android.permission.READ_EXTERNAL_STORAGE");
        permissions_pair.put("android.permission.READ_EXTERNAL_STORAGE", PERMISSION_FILE);
        permissions.put(PERMISSION_CAMERA, "android.permission.CAMERA");
        permissions_pair.put("android.permission.CAMERA", PERMISSION_CAMERA);
    }

    public interface PermissionCallback {
        public void onSuccess();

        public void goSetting();

        public void onCancel();

        public void onFail(int[] ids);
    }

    public static class Builder {
        PermissionHandler item = null;

        Builder(Context context) {
            item = new PermissionHandler(context);
        }

        public Builder setPermission(int[] ids) {
            item.permissionIds = ids;
            return this;
        }

        public Builder setCallBack(PermissionCallback callBack) {
            item.callBack = callBack;
            return this;
        }

        public Builder setNegativeString(String str) {
            item.setNegativeString(str);
            return this;
        }

        public void request() {
            if (item.hasAllPermission()) {
                if (item.callBack != null) {
                    item.callBack.onSuccess();
                }
            } else {
                item.showRequestDialog();
            }
        }
    }

    public static Builder createBuilder(Context context) {
        return new Builder(context);
    }

    Context context;
    int[] permissionIds;
    PermissionCallback callBack;
    String negative = null;
    String positive = null;

    public PermissionHandler(Context context) {
        this.context = context;
        negative = "取消";
        positive = "确定";
    }

    void setNegativeString(String s) {
        negative = s;
    }

    boolean hasAllPermission() {
        for (int i : permissionIds) {
            if (!PermissionInterceptor.hasPermission(context, permissions.get(i))) {
                return false;
            }
        }
        return true;
    }

    void showRequestDialog() {
        deleteNotNeed();
        AlertDialog.Builder build = new AlertDialog.Builder(context);
        build.setIcon(R.mipmap.icon_launchericon);
        build.setTitle("权限请求");
        build.setMessage(getString());

        final boolean result = needRequst();
        if (!result) {
            positive = "设置";
        }

        build.setPositiveButton(positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (result) {
                    request();
                } else {
                    if (!toSetting()) {
                        if (callBack != null) {
                            callBack.onFail(permissionIds);
                        }
                    }
                }
                dialog.dismiss();
            }
        });
        build.setNegativeButton(negative, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (callBack != null) {
                    callBack.onCancel();
                }
                dialog.dismiss();
            }
        });
        Dialog dialog = build.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private boolean toSetting() {
        try {
            if (callBack != null) {
                callBack.goSetting();
            }
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent.setData(uri);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getString() {
        StringBuilder sb = new StringBuilder("");
        char nextLine = '\n';
        for (int i : permissionIds) {
            sb.append(context.getString(i)).append(nextLine);
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private void deleteNotNeed() {
        List<Integer> is = new ArrayList<Integer>(permissionIds.length);
        for (int i : permissionIds) {
            if (!PermissionInterceptor.hasPermission(context, permissions.get(i))) {
                is.add(i);
            }
        }
        int[] ids = new int[is.size()];
        for (int i = 0; i < ids.length; i++) {
            ids[i] = is.get(i);
        }
        this.permissionIds = ids;
    }

    private boolean needRequst() {
        SharedPreferences sp = context.getSharedPreferences(SPNAME, 0);
        for (int i : permissionIds) {
            if (sp.getBoolean(String.valueOf(i), true)) {
                return true;
            } else {
                if (PermissionInterceptor.shouldShowRequestPermissionRationale(context, permissions.get(i))) {
                    return true;
                }
            }
        }
        return false;
    }


    private void request() {
        SharedPreferences sp = context.getSharedPreferences(SPNAME, 0);
        SharedPreferences.Editor editor = sp.edit();
        String[] ss = new String[permissionIds.length];
        for (int i = 0; i < permissionIds.length; i++) {
            editor.putBoolean(String.valueOf(permissionIds[i]), false);
            ss[i] = permissions.get(permissionIds[i]);
        }
        editor.commit();
        PermissionInterceptor.requestPermissions(context, ss, this);
    }

    public void onSuccess() {
        if (callBack == null) {
            return;
        }
        callBack.onSuccess();
    }

    public void onFail(List<String> dined) {
        if (callBack == null) {
            return;
        }
        int[] ids = new int[dined.size()];
        int j = 0;
        for (String s : dined) {
            ids[j++] = permissions_pair.get(s);
        }
        callBack.onFail(ids);

    }


}
