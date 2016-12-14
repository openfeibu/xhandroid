package cn.flyexp.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.flyexp.R;
import cn.flyexp.util.ScreenHelper;

/**
 * Created by tanxinye on 2016/10/31.
 */
public class LoadingMoreFooter extends LinearLayout {

    private Context context;
    private TextView tvEnd;
    private View loadingLayout;

    public LoadingMoreFooter(Context context) {
        super(context, null);
        this.context = context;
        initView();
    }

    private void initView() {
        setGravity(Gravity.CENTER);
        setBackgroundColor(Color.TRANSPARENT);
        setVisibility(GONE);
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        View loadingFooter = LayoutInflater.from(context).inflate(R.layout.layout_loading_footer, this,true);
        loadingLayout = loadingFooter.findViewById(R.id.loading_layout);
        tvEnd = (TextView) loadingFooter.findViewById(R.id.tv_end);
    }

    public void showEnd() {
        setVisibility(VISIBLE);
        loadingLayout.setVisibility(GONE);
        tvEnd.setVisibility(VISIBLE);
    }

    public void showLoading() {
        setVisibility(VISIBLE);
        loadingLayout.setVisibility(VISIBLE);
        tvEnd.setVisibility(GONE);
    }

}
