package cn.flyexp.view;

import android.animation.Animator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/9/5.
 */
public class VerticalMarqueeView extends View {
    public static final int DURATION_SCROLL = 3000;
    public static final int DURATION_ANIMATOR = 1000;
    private int textSize = 30;
    private int color = Color.BLACK;
    private String[] datas = null;

    private int width;
    private int height;
    private int centerX;
    private int centerY;

    private List<TextBlock> blocks = new ArrayList<>();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Handler handler = new Handler();
    private boolean isStopScroll = false;

    public VerticalMarqueeView(Context context) {
        super(context);
    }

    public VerticalMarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public VerticalMarqueeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VerticalMarqueeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public VerticalMarqueeView color(int color) {
        this.color = color;
        return this;
    }

    public VerticalMarqueeView textSize(int textSize) {
        this.textSize = textSize;
        return this;
    }

    public VerticalMarqueeView datas(String[] datas) {
        this.datas = datas;
        return this;
    }

    public void commit() {
        if (this.datas == null || this.datas.length == 0) {
            throw new IllegalStateException("may be not invoke the method named datas(String[])");
        }
        paint.setColor(color);
        paint.setTextSize(textSize);
    }

    int widthMeasureSpec=0;
    int heightMeasureSpec=0;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        if (this.datas == null || this.datas.length == 0) {
//            return;
//        }
        width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
//        centerX = width / 2;
//        centerY = height / 2;
//        blocks.clear();
//        //添加显示区域的文字
//        TextBlock block1 = new TextBlock(width, height, paint);
////        block1.reset(datas[0], centerX, centerY, 0);
//        block1.reset(datas[0], 0, centerY, 0);
//        blocks.add(block1);
//        if (datas.length > 1) {
//            TextBlock block2 = new TextBlock(width, height, paint);
////            block2.reset(datas[1], centerX, centerY + height, 1);
//            block2.reset(datas[1], 0, centerY + height, 1);
//            blocks.add(block2);
//        }
    }

    public void setData(){
        if (this.datas == null || this.datas.length == 0) {
            return;
        }
        centerX = width / 2;
        centerY = height / 2;
        blocks.clear();
        //添加显示区域的文字
        TextBlock block1 = new TextBlock(width, height, paint);
//        block1.reset(datas[0], centerX, centerY, 0);
        block1.reset(datas[0], 0, centerY, 0);
        blocks.add(block1);
        if (datas.length > 1) {
            TextBlock block2 = new TextBlock(width, height, paint);
//            block2.reset(datas[1], centerX, centerY + height, 1);
            block2.reset(datas[1], 0, centerY + height, 1);
            blocks.add(block2);
        }
    }



    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0; i < blocks.size(); i++) {
            blocks.get(i).draw(canvas);
        }
    }

    public void startScroll() {
        isStopScroll = false;
        if (this.datas == null || this.datas.length == 0) {
            return;
        }
        if (!isStopScroll) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scroll();
                    if (!isStopScroll) {
                        handler.postDelayed(this, DURATION_SCROLL);
                    }
                }
            }, DURATION_SCROLL);
        }
    }

    public void stoScroll() {
        this.isStopScroll = true;
    }
    public VerticalMarqueeView setData(String[] datas){
        this.datas=datas;
        scroll();
        return this;
    }
    private void scroll() {
        ValueAnimator animator = ValueAnimator.ofPropertyValuesHolder(PropertyValuesHolder.ofInt("scrollY", centerY, centerY - height));
        animator.setDuration(DURATION_ANIMATOR);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int scrollY = (int) animation.getAnimatedValue();
                    blocks.get(0).reset(scrollY);
                    blocks.get(1).reset(scrollY + height);
                invalidate();
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //移除第一块
                int position = blocks.get(1).getPosition();
                TextBlock textBlock = blocks.remove(0);
                //最后一个
                if (position == datas.length - 1) {
                    position = 0;
                } else {
                    position++;
                }
                textBlock.reset(datas[position], centerY + height, position);
                blocks.add(textBlock);
                invalidate();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    public int getCurrentPosition() {
        if (datas == null || datas.length == 0) {
            return -1;
        }
        if (datas.length == 1 && blocks.size() == 1) {
            return 0;
        }
        return blocks.get(0).getPosition();
    }

    public class TextBlock {
        private int width;
        private int height;
        private String text;
        private int drawX;
        private int drawY;
        private Paint paint;
        private int position;

        public TextBlock(int width, int height, Paint paint) {
            this.width = width;
            this.height = height;
            this.paint = paint;
        }

        public void reset(int centerY) {
            reset(text, centerX, centerY, position);
        }

        public void reset(String text, int centerY) {
            reset(text, centerX, centerY, position);
        }

        public void reset(String text, int centerY, int position) {
            reset(text, centerX, centerY, position);
        }

        public void reset(String text, int centerX, int centerY, int position) {
            this.text = text;
            this.position = position;
            int measureWidth = (int) paint.measureText(text);
//            drawX = (width - measureWidth) / 2;
            drawX = 0;
            Paint.FontMetrics metrics = paint.getFontMetrics();
            drawY = (int) (centerY + (metrics.bottom - metrics.top) / 2 - metrics.bottom);
        }

        public int getPosition() {
            return position;
        }

        public void draw(Canvas canvas) {
            if (text.length() > 18) {
                text = text.substring(0, 14) + "...";
            }
            canvas.drawText(text, drawX, drawY, paint);
        }
    }
}
