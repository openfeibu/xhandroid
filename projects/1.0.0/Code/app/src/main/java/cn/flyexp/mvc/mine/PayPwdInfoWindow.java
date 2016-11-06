package cn.flyexp.mvc.mine;

import android.view.View;

import cn.flyexp.R;
import cn.flyexp.framework.AbstractWindow;

/**
 * Created by txy on 2016/8/23 0023.
 */
public class PayPwdInfoWindow extends AbstractWindow implements View.OnClickListener {

    private MineViewCallBack callBack;

    public PayPwdInfoWindow(MineViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_paypwdinfo);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.layout_changepwd).setOnClickListener(this);
        findViewById(R.id.layout_resetpwd).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.layout_changepwd:
                callBack.changePayPwdEnter();
                break;
            case R.id.layout_resetpwd:
                callBack.resetPayPwdEnter();
                break;
        }
    }
}
