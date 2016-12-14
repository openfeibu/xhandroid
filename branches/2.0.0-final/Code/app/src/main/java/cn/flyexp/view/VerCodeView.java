package cn.flyexp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.EditText;

import cn.flyexp.R;

/**
 * Created by tanxinye on 2016/12/3.
 */
public class VerCodeView extends EditText {

    private String vercode;

    public VerCodeView(Context context) {
        super(context);
    }

    public VerCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        Paint rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setStrokeWidth(1f);
        rectPaint.setColor(getResources().getColor(R.color.light_gray));
        rectPaint.setStyle(Paint.Style.STROKE);
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(getResources().getDimension(R.dimen.font_large_small));
        textPaint.setColor(getResources().getColor(R.color.font_dark));
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
        float offY = fontTotalHeight / 2 - fontMetrics.bottom;
        int offset = (width - 4 * height) / 3;
        int sx = 0;
        int ex = height;
        for (int i = 0; i < 4; i++) {
            canvas.drawRect(new RectF(sx, 0, ex, height - 1), rectPaint);
            if (!TextUtils.isEmpty(vercode) && i < vercode.length()) {
                canvas.drawText(String.valueOf(vercode.charAt(i)), sx + height / 2, height / 2 + offY, textPaint);
            }
            sx = ex + offset;
            ex = sx + height;
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        vercode = text.toString().trim();
        invalidate();
    }

}
