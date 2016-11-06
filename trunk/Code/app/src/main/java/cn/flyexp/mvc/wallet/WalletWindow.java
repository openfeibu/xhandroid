package cn.flyexp.mvc.wallet;

import android.view.View;
import android.widget.TextView;

import cn.flyexp.R;
import cn.flyexp.entity.WalletResponse;
import cn.flyexp.entity.WebBean;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.mvc.mine.MineViewCallBack;
import cn.flyexp.util.LogUtil;

/**
 * Created by txy on 2016/8/21 0021.
 */
public class WalletWindow extends AbstractWindow implements View.OnClickListener {

    private WalletViewCallBack callBack;
    private TextView tv_money;

    public WalletWindow(WalletViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
        refreshData();
    }

    private void initView() {
        setContentView(R.layout.window_wallet);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.layout_withdrawal).setOnClickListener(this);
        findViewById(R.id.layout_paypwd).setOnClickListener(this);
        findViewById(R.id.layout_detail).setOnClickListener(this);
        findViewById(R.id.layout_walletStatement).setOnClickListener(this);
        findViewById(R.id.layout_payinfo).setOnClickListener(this);
        findViewById(R.id.layout_paypwd).setOnClickListener(this);


        Float balance = WindowHelper.getFloatByPreference("balance");
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_money.setText(String.valueOf(balance));
    }

    public void refreshData() {
        String token = WindowHelper.getStringByPreference("token");
        if (token.equals("")) {
            callBack.loginWindowEnter();
            return;
        }
        callBack.getWallet(token);
    }

    public void responseData(WalletResponse.WalletResponseData responseData) {
        float balance = responseData.getWallet();
        tv_money.setText(balance + "");
        WindowHelper.putStringByPreference("alipay", responseData.getAlipay());
        WindowHelper.putIntByPreference("is_alipay", responseData.getIs_alipay());
        WindowHelper.putIntByPreference("is_paypwd", responseData.getIs_paypassword());
        WindowHelper.putFloatByPreference("balance", responseData.getWallet());
    }

    @Override
    public void onClick(View v) {
        int is_alipay = WindowHelper.getIntByPreference("is_alipay");
        int is_paypwd = WindowHelper.getIntByPreference("is_paypwd");
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.layout_withdrawal:
                int isAuth = WindowHelper.getIntByPreference("is_auth");
                if (isAuth == 0) {
                    WindowHelper.showToast(getContext().getString(R.string.none_certifition));
                    return;
                } else if (isAuth == 2) {
                    WindowHelper.showToast(getContext().getString(R.string.certifing));
                    return;
                }
                if (is_paypwd == 0) {
                    callBack.setPayPwdEnter();
                } else if (is_alipay == 0) {
                    callBack.payBindEnter();
                } else if (is_alipay == 1 && is_paypwd == 1) {
                    callBack.withdrawalEnter();
                }
                break;
            case R.id.layout_detail:
                callBack.walletDetailEnter();
                break;
            case R.id.layout_payinfo:
                if (is_alipay == 0) {
                    callBack.payBindEnter();
                } else if (is_alipay == 1) {
                    callBack.payAccountEnter();
                }
                break;
            case R.id.layout_paypwd:
                if (is_paypwd == 0) {
                    callBack.setPayPwdEnter();
                } else if (is_paypwd == 1) {
                    callBack.payPwdInfoEnter();
                }
                break;
            case R.id.layout_walletStatement:
                WebBean walletWebBean = new WebBean();
                walletWebBean.setRequest(true);
                walletWebBean.setTitle("钱包说明");
                walletWebBean.setName("walletStatement");
                callBack.webWindowEnter(walletWebBean);
                break;
        }
    }
}
