package cn.flyexp.framework;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;


/**
 * Created by zlk on 2016/9/24.
 * window 显示dialog, toast辅助类
 */
public class WindowHelper{
    static Context getContext(){
        return AbstractWindow.activity;
    }

    public static void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }


    public static String getStringByPreference(String key) {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getString(key, "");
    }

    public static void putStringByPreference(String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString(key, value).commit();
    }

    public static boolean getBooleanByPreference(String key) {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(key, false);
    }

    public static void putBooleanByPreference(String key, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean(key, value).commit();
    }

    public static int getIntByPreference(String key) {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getInt(key, -1);
    }

    public static void putIntByPreference(String key, int value) {
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putInt(key, value).commit();
    }

    public static float getFloatByPreference(String key) {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getFloat(key, -1f);
    }

    public static void putFloatByPreference(String key, float value) {
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putFloat(key, value).commit();
    }


    public static void showAlertDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog alertDialog = builder.setMessage(msg).create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    public static void showAlertDialog(String msg, String positiListenerMsg, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog alertDialog = builder.setMessage(msg).setPositiveButton(positiListenerMsg, listener).create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public static void showAlertDialog(String msg, String negatiListenerMsg, String positiListenerMsg, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog alertDialog = builder.setMessage(msg).setPositiveButton(positiListenerMsg, listener).setNegativeButton(negatiListenerMsg, null).create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    public static void showAlertDialog(String msg, String negatiListenerMsg, DialogInterface.OnClickListener negatilistener, String positiListenerMsg, DialogInterface.OnClickListener positilistener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog alertDialog = builder.setMessage(msg).setPositiveButton(positiListenerMsg, positilistener).setNegativeButton(negatiListenerMsg, negatilistener).create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }
}
