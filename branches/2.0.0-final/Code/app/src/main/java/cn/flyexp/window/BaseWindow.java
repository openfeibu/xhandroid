package cn.flyexp.window;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import butterknife.ButterKnife;
import cn.flyexp.MainActivity;
import cn.flyexp.R;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.framework.ControllerManager;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowManager;
import cn.flyexp.view.SwipeBackLayout;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/10/21.
 */
public abstract class BaseWindow extends SwipeBackLayout implements BaseResponseCallback {

    private static WindowManager windowManager = WindowManager.getInstance((Activity) MainActivity.getContext());
    private static ControllerManager controllerManager = ControllerManager.getInstance();
    private static NotifyManager notifyManager = NotifyManager.getInstance();

    public WindowManager getWindowManager() {
        return windowManager;
    }

    public static NotifyManager getNotifyManager() {
        return notifyManager;
    }

    public BaseWindow() {
        this(null);
    }

    public BaseWindow(Bundle bundle) {
        super(MainActivity.getContext());
        ViewGroup rootView = (ViewGroup) inflate(getContext(), getLayoutId(), this);
        ButterKnife.inject(this);

//        View childView = rootView.getChildAt(0);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            childView.setPadding(0, 0, 0, 0);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            childView.setPadding(0, ScreenHelper.dip2px(getContext(), 25), 0, 0);
//        } else {
//            childView.setPadding(0, 0, 0, 0);
//        }
    }

    protected abstract int getLayoutId();

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        hideKeybroad();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        hideKeybroad();
        if (getConfig() == null || !getConfig().isNoneReset()) {
            ButterKnife.reset(this);
        }
    }

    private void hideKeybroad() {
        InputMethodManager inputmanger = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(getWindowToken(), 0);
    }


    public void onRenew() {
    }

    public void onStart() {
    }

    public void onResume() {
    }

    public void onStop() {
    }

    public boolean onBackPressed() {
        return false;
    }

    public void showWindow(boolean animtion) {
        windowManager.pushWindow(this, animtion);
        onStart();
    }

    public void hideWindow(boolean animtion) {
        windowManager.popWindow(animtion);
    }

    public void clearStack() {
        windowManager.clearStack();
    }

    public void openWindow(int mesId) {
        controllerManager.sendMessage(mesId);
    }

    public void openWindow(int mesId, Bundle bundle) {
        controllerManager.sendMessage(mesId, bundle);
    }

    protected void showToast(int strId) {
        Toast.makeText(getContext(), strId, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void dismissProgressDialog(SweetAlertDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismissWithAnimation();
            dialog.dismiss();
        }
    }

    /**
     * 滑动返回
     * 不需要滑动返回需重写
     *
     * @return
     */
    @Override
    protected boolean isEnabledSwipeBack() {
        return true;
    }

    @Override
    public void finishWindow() {
        windowManager.popWindow(false);
    }

    @Override
    public void requestFailure() {

    }

    @Override
    public void requestFinish() {

    }

    @Override
    public void noConnected() {

    }

    @Override
    public void renewLogin() {
        controllerManager.sendMessage(WindowIDDefine.WINDOW_LOGIN);
    }

    @Override
    public void showDetail(String detail) {
        showToast(detail);
    }

    @Override
    public void showDetail(int strid) {
        showToast(strid);
    }

    public class Config {

        public final int[] ANIMSTYLE_SLIDE = new int[]{R.anim.push_slideup, R.anim.push_slidedown};
        public final int[] ANIMSTYLE_FADE = new int[]{R.anim.push_fadein, R.anim.push_fadeout};

        private int[] animStyle = new int[2];
        private boolean isStateBarEnable = true;
        private int stateBarColor;
        private boolean isNoneReset;

        public boolean isNoneReset() {
            return isNoneReset;
        }

        public void setNoneReset(boolean noneReset) {
            isNoneReset = noneReset;
        }

        public int[] getAnimStyle() {
            return animStyle;
        }

        public void setAnimStyle(int[] animStyle) {
            this.animStyle = animStyle;
        }

        public boolean isStateBarEnable() {
            return isStateBarEnable;
        }

        public void setStateBarEnable(boolean stateBarEnable) {
            isStateBarEnable = stateBarEnable;
        }

        public int getStateBarColor() {
            return stateBarColor;
        }

        public void setStateBarColor(int stateBarColor) {
            this.stateBarColor = stateBarColor;
        }
    }

    private Config config;

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}
