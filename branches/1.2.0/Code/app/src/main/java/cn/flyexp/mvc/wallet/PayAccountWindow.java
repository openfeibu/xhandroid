package cn.flyexp.mvc.wallet;

import android.view.View;
import android.widget.TextView;

import cn.flyexp.R;
import cn.flyexp.entity.AlipayInfoResponse;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.mvc.mine.MineViewCallBack;
import cn.flyexp.util.LogUtil;

/**
 * Created by txy on 2016/8/23 0023.
 */
public class PayAccountWindow extends AbstractWindow implements View.OnClickListener {

    private WalletViewCallBack callBack;
    private TextView tv_account;
    private TextView tv_name;

    public PayAccountWindow(WalletViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
        String token = WindowHelper.getStringByPreference("token");
        if (token.equals("")) {
            callBack.loginWindowEnter();
            return;
        }
        callBack.getAlipayInfo(token);
    }

    private void initView() {
        setContentView(R.layout.window_payaccount);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.btn_change).setOnClickListener(this);

        tv_account = (TextView) findViewById(R.id.tv_account);
        tv_name = (TextView) findViewById(R.id.tv_name);
    }

    public void responseData(AlipayInfoResponse.AlipayInfoResponseData data) {
        String account = data.getAlipay();
        String name = data.getAlipay_name();
        if (account.equals("") || name.equals("")) {
            return;
        }
        StringBuffer sbaccount = new StringBuffer(account);
        for (int i = 3; i < account.length() - 3; i++) {
            sbaccount.setCharAt(i, '*');
        }
        StringBuffer sbname = new StringBuffer(name);
        sbname.setCharAt(1, '*');
        tv_account.setText("账户：" + sbaccount.toString());
        tv_name.setText("姓名：" + sbname.toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.btn_change:
                callBack.changePayAccount();
                break;
        }
    }
}
