package cn.flyexp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by tanxinye on 2016/12/17.
 */
public class RefreshLayout extends PtrFrameLayout {

    public RefreshLayout(Context context) {
        this(context, null);
    }

    public RefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        RefreshHeader refreshHeader = new RefreshHeader(getContext());
        setHeaderView(refreshHeader);
        addPtrUIHandler(refreshHeader);
    }
}
