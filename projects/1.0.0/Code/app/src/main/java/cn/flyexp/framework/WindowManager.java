package cn.flyexp.framework;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.util.Stack;

import cn.flyexp.R;
import cn.flyexp.mvc.topic.TopicDetailWindow;
import cn.flyexp.util.LogUtil;

/**
 * Created by zlk on 2016/3/26.
 */
public class WindowManager {
    private Stack<AbstractWindow> windowStack = new Stack<AbstractWindow>();//后面会有用
    private ViewGroup container = null;//view的根
    private Activity activity;
    private long lastTime;

    public WindowManager(Activity a) {
        this.activity = a;
        container = new FrameLayout(activity);
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        init();
    }

    private void init() {
        activity.setContentView(container);
    }

    /**
     * 动画同学们加上去
     * isStack 加栈表示要可以finish当前window
     */
    public void pushWindow(AbstractWindow window, boolean isStack, boolean animation) {
        ViewGroup parent = (ViewGroup) window.getParent();
        if (parent != null) {
            parent.removeView(window);
        }
        ViewGroup.LayoutParams layoutParams = window.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        if (isStack) {
            windowStack.push(window);
        }
        if (animation) {
            window.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.push_left_in));
        }
        container.addView(window, layoutParams);

    }

    public void popWindow(boolean animation) {
        if (windowStack.size() > 0) {
            AbstractWindow window = windowStack.pop();
            if (animation) {
                window.setAnimation(AnimationUtils.loadAnimation(activity, R.anim.push_right_out));
            }
            container.removeView(window);
            System.gc();
        } else {
            long curTime = System.currentTimeMillis();
            if (curTime - lastTime > 1000) {
                Toast.makeText(activity, "再点一次退出", Toast.LENGTH_SHORT).show();
                lastTime = curTime;
            } else {
                activity.finish();
            }
        }
    }

    public void clearWindow() {
        windowStack.clear();
    }
}
