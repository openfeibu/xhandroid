package cn.flyexp.framework;

import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

import cn.flyexp.FBApplication;
import cn.flyexp.util.LogUtil;

/**
 * Created by txy on 2016/8/19 0019.
 */
public class XGPush {

    public static void init() {
        XGPushManager.registerPush(FBApplication.APPLICATION_CONTEXT, "-1", new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                PreferenceManager.getDefaultSharedPreferences(FBApplication.APPLICATION_CONTEXT).edit().putString("device_token", (String) o).commit();
                LogUtil.error(XGPush.class, "device_token" + o);
            }

            @Override
            public void onFail(Object o, int i, String s) {
            }
        });
    }

    public static void registerPush(String openid) {
        if (openid == null || openid.equals("")) {
            return;
        }
        XGPushManager.registerPush(FBApplication.APPLICATION_CONTEXT, openid, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                LogUtil.error(XGPush.class, "token" + o);
                PreferenceManager.getDefaultSharedPreferences(FBApplication.APPLICATION_CONTEXT).edit().putBoolean("notifiy", true).commit();
            }

            @Override
            public void onFail(Object o, int i, String s) {
                LogUtil.error(XGPush.class, "失败" + o);
            }
        });
    }

    public static void unbundingPush() {
        XGPushManager.registerPush(FBApplication.APPLICATION_CONTEXT, "*");
    }

    public static void unregisterPush() {
        XGPushManager.unregisterPush(FBApplication.APPLICATION_CONTEXT);
    }
}
