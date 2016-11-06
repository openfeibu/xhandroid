package cn.flyexp.framework;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

import cn.flyexp.R;
import cn.flyexp.entity.WebBean;
import cn.flyexp.util.LogUtil;
import cn.flyexp.view.LoadingDialog;


/**
 * Created by zlk on 2016/3/26.
 */
public class AbstractWindow extends FrameLayout {
    static WindowManager windowManager = null;
    static Activity activity = null;
    private ProgressDialog progressDialog;
    final static int REQUEST_PERIOD = 60 * 1000;//1min内不能多次请求
    private long lastRequestTime = -1;

    public interface WindowCallBack {

        void onWindowShow(AbstractWindow window);

        void onWindowHide(AbstractWindow window);

        //用户统计
        void userCount(String key);

        //跳转登录页面
        void loginWindowEnter();

        //跳转他人页面
        void taWindowEnter(String taId);

        //跳转网页窗口
        void webWindowEnter(WebBean bean);

        void setUIData(int key, String data);

        String getUIData(int key);

        static class UIDataKeysDef {
            static int base = 0x7527;
            public static int LOGIN_PHONE_NUM = base ++;
            public static int TOPIC_CONTENT = base ++;
            public static int TASK_CONTENT = base ++;
            public static int REPLY_CONTENT = base ++;
            public static int JOINASSN_NAME = base ++;
            public static int JOINASSN_PHONE = base ++;
            public static int JOINASSN_PRO = base ++;
            public static int JOINASSN_CAUSE = base ++;
        }

    }

    private WindowCallBack windowCallBack;

    public AbstractWindow(WindowCallBack callBack) {
        super(activity);
        this.windowCallBack = callBack;
    }

    /**
     * 默认带动画
     */
    public void showWindow() {
        showWindow(true);
    }

    public void clearWithMe() {
        windowManager.clearWithARootWindow(this);
    }

    public void showWindow(boolean ani) {
        windowManager.pushWindow(this, ani);
        this.setEnabled(true);
        this.setClickable(true);
        this.setFocusable(true);
        this.setFocusableInTouchMode(true);
        if (this.windowCallBack != null) {
            windowCallBack.onWindowShow(this);
        }
    }

    protected void setRequestTimeNow() {
        this.lastRequestTime = System.currentTimeMillis();
    }

    protected boolean canRequest() {
        return System.currentTimeMillis() - this.lastRequestTime - REQUEST_PERIOD > 0;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {
            int keyCode = event.getKeyCode();
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                return windowManager.popWindow(true);
            }
        }
        return super.dispatchKeyEvent(event);
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

    public void exitApp() {
        windowManager.exitApp();
    }

    public View getView(int layoutId) {
        return inflate(getContext(), layoutId, this);
    }

    public void setContentView(int layoutId) {
        inflate(getContext(), layoutId, this);
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

    protected <T> T findViewById(View view, int id) {
        return (T) (view.findViewById(id));
    }
}
