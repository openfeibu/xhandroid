package cn.flyexp.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.flyexp.R;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.LogUtil;

/**
 * Created by tanxinye on 2016/10/14.
 */
public class StepView extends LinearLayout {

    private int num = 4;
    private String[] state = new String[]{"发单", "接单", "完成", "结算"};
    private int currIndex;
    private String[] dates;

    public StepView(Context context) {
        super(context, null);
    }

    public StepView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs);
    }

    private void initView() {
        setOrientation(VERTICAL);
        LinearLayout drawableLayout = new LinearLayout(getContext());
        drawableLayout.setOrientation(HORIZONTAL);
        drawableLayout.setGravity(Gravity.CENTER);
        LinearLayout stateLayout = new LinearLayout(getContext());
        stateLayout.setOrientation(HORIZONTAL);
        stateLayout.setGravity(Gravity.CENTER);
        LinearLayout dateLayout = new LinearLayout(getContext());
        dateLayout.setOrientation(HORIZONTAL);
        dateLayout.setGravity(Gravity.CENTER);
        int i = 0;
        for (; i < num - 1; i++) {
            ImageView stateImage = new ImageView(getContext());
            if (i < currIndex) {
                stateImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_selected_green));
            } else {
                stateImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_prompt_gray));
            }
            drawableLayout.addView(stateImage);
            ImageView lineImage = new ImageView(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(CommonUtil.dip2px(getContext(), 8), 0, CommonUtil.dip2px(getContext(), 8), 0);
            lineImage.setLayoutParams(layoutParams);
            if (i == currIndex - 1) {
                lineImage.setImageDrawable(getResources().getDrawable(R.drawable.progressbar_gg));
            } else if (i < currIndex - 1) {
                lineImage.setImageDrawable(getResources().getDrawable(R.drawable.progressbar_green));
            } else {
                lineImage.setImageDrawable(getResources().getDrawable(R.drawable.progressbar_gray));
            }
            drawableLayout.addView(lineImage);
        }
        ImageView stateImage = new ImageView(getContext());
        if (i < currIndex) {
            stateImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_selected_green));
        } else {
            stateImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_prompt_gray));
        }
        drawableLayout.addView(stateImage);

        for (i = 0; i < num; i++) {
            TextView stateTextView = new TextView(getContext());
            stateTextView.setText(state[i]);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(CommonUtil.dip2px(getContext(), 5), 0, CommonUtil.dip2px(getContext(), 5), 0);
            stateTextView.setLayoutParams(layoutParams);
            stateTextView.setGravity(Gravity.CENTER);
            stateTextView.setPadding(5,5,5,5);
            stateTextView.setTextSize(12);
            stateTextView.setWidth(CommonUtil.dip2px(getContext(), 70));
            stateLayout.addView(stateTextView);
        }

        if (dates != null) {
            for (i = 0; i < num; i++) {
                TextView dateTextView = new TextView(getContext());
                dateTextView.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(CommonUtil.dip2px(getContext(), 5), 0, CommonUtil.dip2px(getContext(), 5), 0);
                dateTextView.setLayoutParams(layoutParams);
                dateTextView.setPadding(0, 5, 0, 0);
                dateTextView.setTextSize(10);
                dateTextView.setTextColor(getResources().getColor(R.color.white));
                dateTextView.setBackgroundDrawable(getResources().getDrawable(R.drawable.progresstime));
                if (i < currIndex) {
                    dateTextView.setVisibility(VISIBLE);
                    dateTextView.setText(dates[i]);
                } else {
                    dateTextView.setVisibility(INVISIBLE);
                }
                dateLayout.addView(dateTextView);
            }
        }

        addView(drawableLayout);
        addView(stateLayout);
        addView(dateLayout);
    }

    public void cancel(String cancelTime) {
        state[0] = "取消";
        dates[0] = cancelTime;
        currIndex = 1;
    }

    public void setDate(String[] dates,int len) {
        this.dates = dates;
        currIndex = len;
    }

    public void show() {
        initView();
    }
}
