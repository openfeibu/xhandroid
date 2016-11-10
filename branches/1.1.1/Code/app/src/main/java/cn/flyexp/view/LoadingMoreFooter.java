package cn.flyexp.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.flyexp.R;

/**
 * Created by tanxinye on 2016/10/31.
 */
public class LoadingMoreFooter extends LinearLayout {

    private Context context;
    private LinearLayout loadingView;
    private LinearLayout endView;

    public LoadingMoreFooter(Context context) {
        super(context, null);
        this.context = context;
        initView();
    }

    private void initView() {
        setGravity(Gravity.CENTER);
        setBackgroundColor(Color.TRANSPARENT);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);
        loadingView = new LinearLayout(getContext());
        loadingView.setGravity(Gravity.CENTER);
        endView = new LinearLayout(getContext());
        endView.setGravity(Gravity.CENTER);
        loadingView.addView((new ProgressBar(context, null, android.R.attr.progressBarStyle)));
        TextView tvEnd = new TextView(getContext());
        tvEnd.setTextColor(getResources().getColor(R.color.light_blue));
        tvEnd.setTextSize(getResources().getDimension(R.dimen.font_normal));
        tvEnd.setText(R.string.loading_end_text);
        endView.addView(tvEnd);
        addView(loadingView, params);
        addView(endView, params);
    }

    public void showEnd() {
        setVisibility(VISIBLE);
        loadingView.setVisibility(GONE);
        endView.setVisibility(VISIBLE);
    }

    public void showLoading() {
        setVisibility(VISIBLE);
        loadingView.setVisibility(VISIBLE);
        endView.setVisibility(GONE);
    }

}
