package cn.flyexp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.flyexp.R;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * Created by tanxinye on 2016/12/17.
 */
public class RefreshHeader extends FrameLayout implements PtrUIHandler {

    private Context context;
    private TextView tvHint;
    private int state;

    private static final int STATE_RESET = 0;
    private static final int STATE_PREPARE = 1;
    private static final int STATE_BEGIN = 2;
    private static final int STATE_FINISH = 0;

    public RefreshHeader(Context context) {
        this(context, null);
    }

    public RefreshHeader(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    private void initView() {
        View refreshHeader = LayoutInflater.from(context).inflate(R.layout.layout_refresh_header, null);
        tvHint = (TextView) refreshHeader.findViewById(R.id.tv_hint);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        addView(refreshHeader, params);
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        state = STATE_RESET;
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        state = STATE_PREPARE;
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        state = STATE_BEGIN;
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        state = STATE_FINISH;
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        switch (state) {
            case STATE_PREPARE:
                if (ptrIndicator.getCurrentPercent() < 1.2) {
                    tvHint.setText(R.string.refresh_down);
                } else {
                    tvHint.setText(R.string.refresh_release);
                }
                break;
            case STATE_BEGIN:
                tvHint.setText(R.string.refresh_doing);
                break;
            case STATE_FINISH:
                tvHint.setText(R.string.refresh_complete);
                break;

        }
    }
}
