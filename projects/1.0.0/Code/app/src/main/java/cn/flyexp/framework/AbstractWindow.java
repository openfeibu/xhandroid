package cn.flyexp.framework;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import cn.flyexp.R;
import cn.flyexp.util.LogUtil;


/**
 * Created by zlk on 2016/3/26.
 */
public class AbstractWindow extends FrameLayout {
    static WindowManager windowManager = null;
    static Activity activity = null;
    private ProgressDialog progressDialog;

    public interface WindowCallBack {
        public void onWindowShow(AbstractWindow window);

        public void onWindowHide(AbstractWindow window);

        //用户统计
        public void userCount(String key);

        //跳转登录页面
        public void loginWindowEnter();

        //跳转他人页面
        public void taWindowEnter(String taId);

        //跳转网页窗口
        public void webWindowEnter(String[] webname, int type);

    }

    private WindowCallBack windowCallBack;

    public AbstractWindow(WindowCallBack callBack) {
        super(activity);
        this.windowCallBack = callBack;
    }

    public void showWindow(boolean isStack, boolean ani) {
        windowManager.pushWindow(this, isStack, ani);
        this.setEnabled(true);
        this.setClickable(true);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        if (this.windowCallBack != null) {
            windowCallBack.onWindowShow(this);
        }
    }

    public static boolean onKeyDownEvent(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            windowManager.popWindow(true);
            return true;
        }
        return false;
    }

    @Override
    protected void onDetachedFromWindow() {
        InputMethodManager inputmanger = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(getWindowToken(), 0);
        super.onDetachedFromWindow();
    }

    public void hideWindow(boolean ani) {
        windowManager.popWindow(ani);
        this.setEnabled(false);
        this.setClickable(false);
        this.setFocusable(false);
        this.setFocusableInTouchMode(false);
        if (this.windowCallBack != null) {
            windowCallBack.onWindowHide(this);
        }
    }

    public void clearWindow() {
        windowManager.clearWindow();
    }

    public View getView(int layoutId) {
        return inflate(getContext(), layoutId, this);
    }

    public void setContentView(int layoutId) {
        inflate(getContext(), layoutId, this);
    }

    public String getStringByPreference(String key) {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getString(key, "");
    }

    public void putStringByPreference(String key, String value) {
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putString(key, value).commit();
    }

    public boolean getBooleanByPreference(String key) {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(key, false);
    }

    public void putBooleanByPreference(String key, boolean value) {
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean(key, value).commit();
    }

    public int getIntByPreference(String key) {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getInt(key, -1);
    }

    public void putIntByPreference(String key, int value) {
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putInt(key, value).commit();
    }

    public float getFloatByPreference(String key) {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getFloat(key, -1f);
    }

    public void putFloatByPreference(String key, float value) {
        PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putFloat(key, value).commit();
    }

    public void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void showProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(activity);
        }
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void showAlertDialog(String msg, String positiListenerMsg, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog alertDialog = builder.setMessage(msg).setPositiveButton(positiListenerMsg, listener).create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    public void showAlertDialog(String msg, String negatiListenerMsg, String positiListenerMsg, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog alertDialog = builder.setMessage(msg).setPositiveButton(positiListenerMsg, listener).setNegativeButton(negatiListenerMsg, null).create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    public void showAlertDialog(String msg, String negatiListenerMsg, DialogInterface.OnClickListener negatilistener, String positiListenerMsg, DialogInterface.OnClickListener positilistener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog alertDialog = builder.setMessage(msg).setPositiveButton(positiListenerMsg, positilistener).setNegativeButton(negatiListenerMsg, negatilistener).create();
        alertDialog.setCancelable(true);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }


    protected <T> T findViewById(View view, int id) {
        return (T) (view.findViewById(id));
    }
}
