package cn.flyexp.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import cn.flyexp.R;

/**
 * Created by tanxinye on 2016/11/10.
 */
public class PopupUtil {

    private PopupWindow popupWindow;
    private Context context;
    private boolean isMark;

    public PopupUtil(@NonNull PopupWindow popupWindow) {
        this.popupWindow = popupWindow;
    }

    public PopupUtil(Context context, @NonNull PopupWindow popupWindow, boolean isMark) {
        this.context = context;
        this.popupWindow = popupWindow;
        this.isMark = isMark;
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    public void setOnDismissListener(final PopupWindow.OnDismissListener listenter) {
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (isMark) {
                    backgroundAlpha(1f);
                }
                listenter.onDismiss();
            }
        });
    }

    public void showAsDropDown(View view) {
        if (isMark) {
            backgroundAlpha(0.5f);
        }
        popupWindow.showAsDropDown(view);
    }

    public void showAsDropDown(View view, int x, int y) {
        if (isMark) {
            backgroundAlpha(0.5f);
        }
        popupWindow.showAsDropDown(view, x, y);
    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        if (isMark) {
            backgroundAlpha(0.5f);
        }
        popupWindow.showAtLocation(parent, gravity, x, y);
    }

    public void dismiss() {
        if (!popupWindow.isShowing()) {
            return;
        }
        popupWindow.dismiss();
    }

    private void backgroundAlpha(float f) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = f;
        ((Activity) context).getWindow().setAttributes(lp);
    }

    public static class Builder {

        private View contentView;
        private int width = ViewGroup.LayoutParams.MATCH_PARENT;
        private int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        private int style;

        public Builder(View contentView) {
            this.contentView = contentView;
        }

        public Builder(View contentView, int width, int height) {
            this.contentView = contentView;
            this.width = width;
            this.height = height;
        }

        public Builder setAnimationStyle(int style) {
            this.style = style;
            return this;
        }

        public PopupWindow create() {
            PopupWindow picPopupWindow = new PopupWindow(contentView, width, height);
            picPopupWindow.setFocusable(true);
            picPopupWindow.setTouchable(true);
            picPopupWindow.setOutsideTouchable(true);
            picPopupWindow.setBackgroundDrawable(new ColorDrawable());
            picPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
            return picPopupWindow;
        }
    }

}
