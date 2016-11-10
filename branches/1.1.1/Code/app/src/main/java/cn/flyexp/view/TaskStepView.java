package cn.flyexp.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import cn.flyexp.R;
import cn.flyexp.util.LogUtil;

/**
 * Created by tanxinye on 2016/11/7.
 */
public class TaskStepView extends LinearLayout {

    private Context context;
    private View stepView;
    private ImageView[] imgState = new ImageView[3];
    private TextView[] tvState = new TextView[4];
    private TextView[] tvDate = new TextView[4];

    public TaskStepView(Context context) {
        this(context, null);
    }

    public TaskStepView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TaskStepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        stepView = LayoutInflater.from(context).inflate(R.layout.layout_taskstep, null);
        imgState[0] = (ImageView) stepView.findViewById(R.id.img_state1);
        imgState[1] = (ImageView) stepView.findViewById(R.id.img_state2);
        imgState[2] = (ImageView) stepView.findViewById(R.id.img_state3);

        tvState[0] = (TextView) stepView.findViewById(R.id.tv_state1);
        tvState[1] = (TextView) stepView.findViewById(R.id.tv_state2);
        tvState[2] = (TextView) stepView.findViewById(R.id.tv_state3);
        tvState[3] = (TextView) stepView.findViewById(R.id.tv_state4);

        tvDate[0] = (TextView) stepView.findViewById(R.id.tv_date1);
        tvDate[1] = (TextView) stepView.findViewById(R.id.tv_date2);
        tvDate[2] = (TextView) stepView.findViewById(R.id.tv_date3);
        tvDate[3] = (TextView) stepView.findViewById(R.id.tv_date4);

        setGravity(Gravity.CENTER);
        addView(stepView);
    }

    public void showData(ArrayList<String> datas) {
        int len = datas.size();
        if (len == 0) {
            return;
        }
        for (int i = 0; i < 3; i++) {
            if (len - 1 == i) {
                imgState[i].setImageDrawable(getResources().getDrawable(R.mipmap.progressbar_gg));
            } else if (len - 1 > i) {
                imgState[i].setImageDrawable(getResources().getDrawable(R.mipmap.progressbar_green));
            } else {
                imgState[i].setImageDrawable(getResources().getDrawable(R.mipmap.progressbar_gray));
            }
        }
        for (int i = 0; i < 4; i++) {
            if (len - 1 >= i) {
                tvState[i].setTextColor(getResources().getColor(R.color.light_green));
                tvDate[i].setText(datas.get(i));
                tvDate[i].setVisibility(VISIBLE);
            } else {
                tvState[i].setTextColor(getResources().getColor(R.color.font_dark));
                tvDate[i].setVisibility(GONE);
            }
        }
    }

    public void showCancel(String cancelTime) {
        tvState[0].setText(getResources().getString(R.string.task_step_cancel));
        tvState[0].setTextColor(getResources().getColor(R.color.font_light));

        tvDate[0].setText(cancelTime);
        tvDate[1].setVisibility(GONE);
        tvDate[2].setVisibility(GONE);
        tvDate[3].setVisibility(GONE);
    }

}
