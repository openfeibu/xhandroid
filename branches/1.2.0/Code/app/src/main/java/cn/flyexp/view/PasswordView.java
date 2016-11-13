package cn.flyexp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by tanxinye on 2016/9/26.
 */
public class PasswordView extends EditText {

    private int textLength;

    public PasswordView(Context context) {
        super(context,null);
    }

    public PasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStrokeWidth(2f);

        RectF rect = new RectF(0, 0, width, height);
        paint.setColor(0xFFCCCCCC);
        canvas.drawRoundRect(rect, 5, 5, paint);

        RectF rectIn = new RectF(rect.left + 5, rect.top + 5, rect.right - 5, rect.bottom - 5);
        paint.setColor(Color.WHITE);
        canvas.drawRoundRect(rectIn, 5, 5, paint);

        paint.setColor(0xFFCCCCCC);
        paint.setStrokeWidth(3);
        for (int i = 1; i < 6; i++) {
            float x = width * i / 6;
            canvas.drawLine(x, 0, x, height, paint);
        }

        float cx, cy = height / 2;
        float half = width / 6 / 2;
        for (int i = 0; i < textLength; i++) {
            cx = width * i / 6 + half;
            canvas.drawCircle(cx, cy, 8, paint);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        this.textLength = text.toString().length();
        invalidate();
    }
}
