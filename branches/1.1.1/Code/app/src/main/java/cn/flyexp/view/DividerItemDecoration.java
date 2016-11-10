package cn.flyexp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import cn.flyexp.R;
import cn.flyexp.util.ScreenHelper;

/**
 * Created by tanxinye on 2016/10/31.
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {


    private Paint paint;
    private int itemSize;

    public DividerItemDecoration(Context context) {
        itemSize = ScreenHelper.dip2px(context, 1);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.light_gray));
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int left = parent.getPaddingLeft();
        int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        int childSize = parent.getChildCount() - 1;
        for (int i = 0; i < childSize; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + layoutParams.bottomMargin;
            int bottom = top + itemSize;
            c.drawRect(left, top, right, bottom, paint);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.set(0, 0, 0, itemSize);
    }
}
