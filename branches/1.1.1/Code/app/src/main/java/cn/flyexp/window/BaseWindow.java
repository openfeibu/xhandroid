package cn.flyexp.window;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import butterknife.ButterKnife;
import cn.flyexp.MainActivity;
import cn.flyexp.R;
import cn.flyexp.callback.BaseResponseCallback;
import cn.flyexp.framework.ControllerManager;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowManager;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/10/21.
 */
public abstract class BaseWindow extends FrameLayout implements BaseResponseCallback {

    private static WindowManager windowManager = WindowManager.getInstance(MainActivity.getActivity());
    private static ControllerManager controllerManager = ControllerManager.getInstance();
    private static NotifyManager notifyManager = NotifyManager.getInstance();
    private float moveX;
    private float downX;
    private boolean isSwipe;

    public WindowManager getWindowManager() {
        return windowManager;
    }

    public BaseWindow() {
        this(null);
    }

    public BaseWindow(Bundle bundle) {
        super(MainActivity.getActivity());
        setBackgroundColor(Color.WHITE);
        inflate(getContext(), getLayoutId(), this);
        ButterKnife.inject(this);
    }

    public static NotifyManager getNotifyManager() {
        return notifyManager;
    }

    protected abstract int getLayoutId();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        if (!isSwipe) {
//            return true;
//        }
//        int action = event.getAction();
//        int windowWidth = ScreenHelper.getScreenWidth(getContext());
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                downX = event.getX();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                moveX = event.getX();
//                if (moveX - downX < 0) {
//                    return true;
//                }
//                scrollBy((int) (moveX - downX),0);
//
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                if ((moveX - downX) < (windowWidth / 2)) {
//                    setTranslationX(0);
//                } else {
//                    hideWindow(false);
//                    setTranslationX(windowWidth);
//                }
//                break;
//        }
//        return super.onTouchEvent(event);
        return true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        InputMethodManager inputmanger = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputmanger.hideSoftInputFromWindow(getWindowToken(), 0);
        ButterKnife.reset(this);
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
        this.isSwipe = animtion;
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

    protected void setStatusBarColor(int color) {
        windowManager.setStatusBarColor(color);
    }

    protected void dismissProgressDialog(SweetAlertDialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismissWithAnimation();
            dialog.dismiss();
        }
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
}
