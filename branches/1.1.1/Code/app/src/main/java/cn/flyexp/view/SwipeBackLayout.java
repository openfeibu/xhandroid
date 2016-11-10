package cn.flyexp.view;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by tanxinye on 2016/10/22.
 */
public class SwipeBackLayout extends FrameLayout {

    private ViewDragHelper viewDragHelper;

    public SwipeBackLayout(Context context) {
        super(context);
        viewDragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            viewDragHelper.cancel();
            return false;
        }
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return false;
    }

    public class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return false;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return Math.min(child.getWidth(), Math.max(left, 0));
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return 200;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            final int childWidth = releasedChild.getWidth();
            invalidate();
        }

        @Override
        public int getViewVerticalDragRange(View child) {
            return 0;
        }
    }
}
