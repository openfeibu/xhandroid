package cn.flyexp.framework;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Stack;

import cn.flyexp.MainActivity;
import cn.flyexp.R;
import cn.flyexp.util.ColorShades;
import cn.flyexp.util.LogUtil;
import cn.flyexp.util.SystemBarTintManager;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/10/21.
 */
public class WindowManager {

    private Activity activity;
    private ViewGroup container;
    private Stack<BaseWindow> windowStack = new Stack<BaseWindow>();
    private static WindowManager windowManager;
    private long lastTime;
    private SystemBarTintManager systemBarTintManager;
    private ColorShades colorShades;
    private long duration = 200;

    private WindowManager(Activity activity) {
        this.activity = activity;
        container = new FrameLayout(activity);
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        activity.setContentView(container);
        systemBarTintManager = new SystemBarTintManager(MainActivity.getActivity());
        systemBarTintManager.setStatusBarTintEnabled(true);
        colorShades = new ColorShades();
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
            Animation inAnim = AnimationUtils.loadAnimation(activity, R.anim.push_in);
            inAnim.setInterpolator(new DecelerateInterpolator());
            window.setAnimation(inAnim);
            if (windowStack.size() != 0 && windowStack.peek() != null) {
                BaseWindow topWindow = windowStack.peek();
                int lastColor = getWindowStatusBarColor(topWindow);
                int currColor = getWindowStatusBarColor(window);
                countDownChangeStatusBarColor(lastColor, currColor);
            }
        } else {
            setDefalutStatusBarColor(getWindowStatusBarColor(window));
        }
        onStop();
        container.addView(window, layoutParams);
        windowStack.push(window);
    }

    public void popWindow(boolean animation) {
        final BaseWindow window = windowStack.pop();
        if (animation) {
            Animation outAnim = AnimationUtils.loadAnimation(activity, R.anim.push_out);
            outAnim.setInterpolator(new DecelerateInterpolator());
            window.setAnimation(outAnim);
            if (windowStack.size() != 0 && windowStack.peek() != null) {
                BaseWindow headWindow = windowStack.peek();
                int lastColor = getWindowStatusBarColor(window);
                int currColor = getWindowStatusBarColor(headWindow);
                countDownChangeStatusBarColor(lastColor, currColor);
            }
        } else {
            if (windowStack.size() != 0 && windowStack.peek() != null) {
                setDefalutStatusBarColor(getWindowStatusBarColor(windowStack.peek()));
            }
        }
        container.removeView(window);
        onResume();
    }

    private void countDownChangeStatusBarColor(final int fromColor, final int toColor) {
        if (fromColor == toColor) {
            return;
        }
        new CountDownTimer(duration, 1) {

            @Override
            public void onTick(long millisUntilFinished) {
                changeColor(fromColor, toColor, (1 - (float) millisUntilFinished / duration));
            }

            @Override
            public void onFinish() {
            }
        }.start();
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

    public void changeColor(int lastColor, int currColor, float shade) {
        colorShades.setFromColor(lastColor)
                .setToColor(currColor)
                .setShade(shade);
        setStatusBarColor(colorShades.generate());
    }

    public void setStatusBarColor(int color) {
        systemBarTintManager.setTintColor(color);
    }

    private void setDefalutStatusBarColor(int color) {
        systemBarTintManager.setStatusBarTintColor(color);
    }

    private int getWindowStatusBarColor(BaseWindow window) {
        int color = 0;
        View headView = window.findViewById(R.id.headview);
        if (headView != null) {
            ColorDrawable colorDrawable = (ColorDrawable) headView.getBackground();
            if (colorDrawable != null) {
                color = colorDrawable.getColor();
            } else {
                color = Color.BLACK;
            }
        } else {
            color = Color.BLACK;
        }
        return color;
    }


    public void clearStack() {
        windowStack.clear();
    }

    public void exitApp() {
        activity.finish();
        System.exit(0);
    }

    public void onBackPressed() {
        if (!windowStack.peek().onBackPressed()) {
            if (windowStack.size() == 1) {
                long curTime = System.currentTimeMillis();
                if (curTime - lastTime > 1000) {
                    Toast.makeText(activity, R.string.hint_exit_app, Toast.LENGTH_SHORT).show();
                    lastTime = curTime;
                } else {
                    exitApp();
                }
            } else {
                popWindow(true);
            }
        }
    }
}
