package cn.flyexp.mvc.mine;

import android.view.View;

import cn.flyexp.R;
import cn.flyexp.framework.AbstractWindow;

/**
 * Created by txy on 2016/8/8 0008.
 */
public class ChangePhoneWindow extends AbstractWindow implements View.OnClickListener {

    private MineViewCallBack callBack;

    public ChangePhoneWindow(MineViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_changephone);
        findViewById(R.id.iv_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
        }
    }
}
