package cn.flyexp.util;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.Set;

import cn.flyexp.MainActivity;
import cn.flyexp.XiaohuiApplication;

/**
 * Created by tanxinye on 2016/10/22.
 */
public class SharePresUtil {

    public static final String KEY_FIRST_RUN = "first_run";
    public static final String KEY_CRASH = "crash";
    public static final String KEY_MID = "mid";
    public static final String KEY_TOKEN = "token";
    public static final String KEY_DEVICE_TOKEN = "device_token";
    public static final String KEY_COOKIE = "cookie";
    public static final String KEY_PUSH_TYPE = "push_type";
    public static final String KEY_BALANCE = "balance";
    public static final String KEY_ALIPAYNAME = "alipay_name";
    public static final String KEY_COLLEGE = "college";
    public static final String KEY_SETPAYACCOUNT = "set_payaccount";
    public static final String KEY_SETPAYPWD = "set_paypwd";
    public static final String KEY_AUTH = "auth";
    public static final String KEY_LAST_TIME_UPDATE = "lastTimeUpdate";
    public static final String KEY_NICK_NAME = "nickname";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_OPENID = "openid";
    public static final String KEY_ACTI_NOTICE = "acti_notice";
    public static final String KEY_LAST_PHONE = "last_phone";
    public static final String KEY_LAST_AVATAR = "last_avatar";

    private static Context getContext() {
        return XiaohuiApplication.getApplication().getApplicationContext();
    }

    public static String getString(String key) {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getString(key, "");
    }

    public static void putString(String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString(key, value).commit();
    }

    public static void putInt(String key, int value) {
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putInt(key, value).commit();
    }

    public static Set<String> getStringSet(String key) {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getStringSet(key, null);
    }

    public static void putStringSet(String key, Set<String> set) {
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putStringSet(key, set).commit();
    }

    public static boolean getBoolean(String key) {
        return PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext()).getBoolean(key, false);
    }

    public static void putBoolean(String key, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean(key, value).commit();
    }

    public static int getInt(String key) {
        return PreferenceManager.getDefaultSharedPreferences(MainActivity.getContext()).getInt(key, 0);
    }

    public static float getFloat(String key) {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getFloat(key, -1f);
    }

    public static void putFloat(String key, float value) {
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putFloat(key, value).commit();
    }

    public static long getLong(String key) {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getLong(key, -1);
    }

    public static void putLong(String key, long value) {
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putLong(key, value).commit();
    }

}
