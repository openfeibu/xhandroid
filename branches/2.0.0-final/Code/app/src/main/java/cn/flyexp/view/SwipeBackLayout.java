package cn.flyexp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import cn.flyexp.R;
import cn.flyexp.util.LogUtil;

/**
 * Created by tanxinye on 2016/12/8.
 */
public abstract class SwipeBackLayout extends FrameLayout {

    private static final int MIN_FLING_VELOCITY = 400;

    private boolean isEnable = true;

    private int edgeFlags = ViewDragHelper.EDGE_LEFT;

    private ViewDragHelper dragHelper;

    private float scrollPercent;

    private float scrollThreshold = 0.3f;

    private float scrimOpacity;

    private int scrimColor = 0x99000000;
    private Drawable shadowDrawable;

    protected abstract void finishWindow();

    protected abstract boolean isEnabledSwipeBack();

    public SwipeBackLayout(Context context) {
        this(context, null);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeBackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
        dragHelper = ViewDragHelper.create(this, new ViewDragCallback());

        float density = getResources().getDisplayMetrics().density;
        float minVel = MIN_FLING_VELOCITY * density;
        dragHelper.setMinVelocity(minVel);
        dragHelper.setMaxVelocity(minVel * 2f);

        shadowDrawable = getResources().getDrawable(R.drawable.shadow_left);

        isEnable = isEnabledSwipeBack();
        dragHelper.setEdgeTrackingEnabled(edgeFlags);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isEnable) {
            return false;
        }
        try {
            return dragHelper.shouldInterceptTouchEvent(ev);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnable) {
            return false;
        }
        dragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean ret = super.drawChild(canvas, child, drawingTime);
        if (dragHelper.getViewDragState() != ViewDragHelper.STATE_IDLE) {
            drawShadow(canvas, child);
            drawScrim(canvas, child);
        }
        return ret;
    }

    private void drawShadow(Canvas canvas, View child) {
        final Rect childRect = new Rect();
        child.getHitRect(childRect);

        shadowDrawable.setBounds(childRect.left - shadowDrawable.getIntrinsicWidth(), childRect.top,
                childRect.left, childRect.bottom);
        shadowDrawable.setAlpha((int) (scrimOpacity * 255));
        shadowDrawable.draw(canvas);
    }

    private void drawScrim(Canvas canvas, View child) {
        final int baseAlpha = (scrimColor & 0xff000000) >>> 24;
        final int alpha = (int) (baseAlpha * scrimOpacity);
        final int color = alpha << 24 | (scrimColor & 0xffffff);

        canvas.clipRect(0, 0, child.getLeft(), getHeight());
        canvas.drawColor(color);
    }

    @Override
    public void computeScroll() {
        if(!isEnable){
            return;
        }
        scrimOpacity = 1 - scrollPercent;
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private class ViewDragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View view, int i) {
            boolean ret = dragHelper.isEdgeTouched(dragHelper.EDGE_LEFT, i);
            boolean directionCheck = dragHelper.checkTouchSlop(ViewDragHelper.DIRECTION_HORIZONTAL, i);
            return ret & directionCheck;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return dragHelper.EDGE_LEFT;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            scrollPercent = Math.abs((float) left / (getWidth() + shadowDrawable.getIntrinsicWidth()));
            invalidate();
            if (scrollPercent >= 1) {
                finishWindow();
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int childWidth = releasedChild.getWidth();
            int left = xvel > 0 || xvel == 0 && scrollPercent > scrollThreshold ?
                    childWidth + shadowDrawable.getIntrinsicWidth() + 10 : 0;
            dragHelper.settleCapturedViewAt(left, 0);
            invalidate();
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return Math.min(child.getWidth(), Math.max(left, 0));
        }
    }
}
