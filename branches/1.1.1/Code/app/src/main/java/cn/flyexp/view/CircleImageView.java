package cn.flyexp.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by tanxinye on 2016/11/2.
 */
public class CircleImageView extends ImageView {


    private Context context;
    private Matrix matrix;
    private Paint bitmapPaint;
    private int width;
    private int radius;
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private BitmapShader bitmapShader;

    public CircleImageView(Context context) {
        this(context, null);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    private void initView() {
        matrix = new Matrix();
        bitmapPaint = new Paint();
        bitmapPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = Math.min(getMeasuredWidth(), getMeasuredHeight());
        radius = width / 2;
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        setUpShader();
        canvas.drawCircle(radius, radius, radius, bitmapPaint);
    }

    private void setUpShader() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        Bitmap bitmap = getBitmapFromDrawable(drawable);
        if (bitmap == null) {
            invalidate();
            return;
        }

        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        int bSize = Math.min(bitmap.getWidth(),bitmap.getHeight());
        scale = width * 1.0f /bSize;
        matrix.setScale(scale,scale);
        bitmapShader.setLocalMatrix(matrix);
        bitmapPaint.setShader(bitmapShader);
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;
            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(2, 2, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
