package cn.flyexp.mvc.shop;

import android.view.View;

import cn.flyexp.R;
import cn.flyexp.framework.AbstractWindow;

/**
 * Created by txy on 2016/7/23 0023.
 */
public class ShopDetailWindow extends AbstractWindow implements View.OnClickListener {

    private ShopViewCallBack callBack;

    public ShopDetailWindow(ShopViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_shop_detail);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.btn_confirm:
                callBack.taskPublishEnter();
                break;
        }
    }
}
