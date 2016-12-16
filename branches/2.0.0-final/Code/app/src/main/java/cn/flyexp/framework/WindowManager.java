package cn.flyexp.framework;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Stack;

import cn.flyexp.MainActivity;
import cn.flyexp.R;
import cn.flyexp.util.ColorShades;
import cn.flyexp.util.LogUtil;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/10/21.
 */
public class WindowManager {

    private Activity activity;
    private ViewGroup container;
    private Stack<BaseWindow> windowStack = new Stack<BaseWindow>();
    private static WindowManager windowManager;

    private WindowManager(Activity activity) {
        this.activity = activity;
        container = new FrameLayout(activity);
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        activity.setContentView(container);
    }

    public static WindowManager getInstance(Activity activity) {
        if (windowManager == null) {
            synchronized (WindowManager.class) {
                if (windowManager == null) {
                    windowManager = new WindowManager(activity);
                }
            }
        }
        return windowManager;
    }

    public void pushWindow(final BaseWindow window, boolean animation) {
        ViewGroup parent = (ViewGroup) window.getParent();
        if (parent != null) {
            parent.removeView(window);
            windowStack.remove(window);
        }
        ViewGroup.LayoutParams layoutParams = window.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        if (animation) {
            BaseWindow.Config builder = window.getConfig();
            Animation inAnim = null;
            if (builder == null) {
                inAnim = AnimationUtils.loadAnimation(activity, R.anim.push_in);
            } else {
                int[] animStyle = builder.getAnimStyle();
                inAnim = AnimationUtils.loadAnimation(activity, animStyle[0]);
            }
            inAnim.setInterpolator(new DecelerateInterpolator());
            window.setAnimation(inAnim);
        }
        onStop();
        container.addView(window, layoutParams);
        windowStack.push(window);
    }

    public void popWindow(boolean animation) {
        final BaseWindow window = windowStack.pop();
        if (animation) {
            BaseWindow.Config builder = window.getConfig();
            Animation outAnim = null;
            if (builder == null) {
                outAnim = AnimationUtils.loadAnimation(activity, R.anim.push_out);
            } else {
                int[] animStyle = builder.getAnimStyle();
                outAnim = AnimationUtils.loadAnimation(activity, animStyle[1]);
            }
            window.setAnimation(outAnim);
        }
        container.removeView(window);
        onResume();
    }

    public void onResume() {
        if (windowStack.size() != 0) {
            windowStack.peek().onResume();
        }
    }

    public void onStop() {
        if (windowStack.size() != 0) {
            windowStack.peek().onStop();
        }
    }

    public void clearStack() {
        container.removeAllViews();
        windowStack.clear();
    }

    public void exitApp() {
        activity.finish();
    }

    public void onBackPressed() {
        if (windowStack.peek() != null && !windowStack.peek().onBackPressed()) {
            if (windowStack.size() == 1) {
                activity.moveTaskToBack(true);
            } else {
                popWindow(true);
            }
        }
    }
}
