package cn.flyexp.window.wallet;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.wallet.WalletCallback;
import cn.flyexp.entity.TokenRequest;
import cn.flyexp.entity.WalletInfoResponse;
import cn.flyexp.entity.WebBean;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.NotifyManager;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.wallet.WalletPresenter;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/10/30.
 */
public class WalletWindow extends BaseWindow implements NotifyManager.Notify,WalletCallback.ResponseCallback {

    @InjectView(R.id.tv_money)
    TextView tvMoney;
    @InjectView(R.id.layout_setpaypwd)
    View setPayPwdView;
    @InjectView(R.id.layout_changepwd)
    View changePwdView;
    @InjectView(R.id.layout_resetpaypwd)
    View resetPayPwdView;
    private WalletPresenter walletPresenter;
    private int setPayAccount;
    private int setPayPwd;
    private int auth;

    @Override
    protected int getLayoutId() {
        return R.layout.window_wallet;
    }

    public WalletWindow() {
        walletPresenter = new WalletPresenter(this);
        getNotifyManager().register(NotifyIDDefine.NOTICE_WALLET,this);
        float balance = SharePresUtil.getFloat(SharePresUtil.KEY_BALANCE);
        setPayAccount = SharePresUtil.getInt(SharePresUtil.KEY_SETPAYACCOUNT);
        setPayPwd = SharePresUtil.getInt(SharePresUtil.KEY_SETPAYPWD);
        auth = SharePresUtil.getInt(SharePresUtil.KEY_AUTH);

        if (setPayPwd == 0) {
            changePwdView.setVisibility(GONE);
            resetPayPwdView.setVisibility(GONE);
            setPayPwdView.setVisibility(VISIBLE);
        } else {
            changePwdView.setVisibility(VISIBLE);
            resetPayPwdView.setVisibility(VISIBLE);
            setPayPwdView.setVisibility(GONE);
        }
        tvMoney.setText(String.valueOf(balance));
    }

    private void readyWallet() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            openWindow(WindowIDDefine.WINDOW_LOGIN);
        } else {
            walletPresenter.requestWalletInfo(new TokenRequest(token));
        }
    }

    @OnClick({R.id.img_back, R.id.layout_withdrawal, R.id.layout_detail, R.id.layout_payinfo, R.id.layout_setpaypwd,
            R.id.layout_changepwd, R.id.layout_resetpaypwd, R.id.layout_statement})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.layout_withdrawal:
                if (auth == 0) {
                    showToast(R.string.go_auth);
                    openWindow(WindowIDDefine.WINDOW_CERTIFITION);
                } else if (auth == 2) {
                    showToast(R.string.authing);
                } else if (auth == 1) {
                    if (setPayPwd == 0) {
                        openWindow(WindowIDDefine.WINDOW_WALLET_SETPAYPWD);
                    } else if (setPayAccount == 0) {
                        openWindow(WindowIDDefine.WINDOW_WALLET_BINDPAYPWD);
                    } else if (setPayAccount == 1 && setPayPwd == 1) {
                        openWindow(WindowIDDefine.WINDOW_WALLET_WITHDRAWAL);
                    }
                }
                break;
            case R.id.layout_detail:
                openWindow(WindowIDDefine.WINDOW_WALLET_DETAIL);
                break;
            case R.id.layout_payinfo:
                if (setPayAccount == 0) {
                    openWindow(WindowIDDefine.WINDOW_WALLET_BINDPAYPWD);
                } else if (setPayAccount == 1) {
                    openWindow(WindowIDDefine.WINDOW_WALLET_PAYACCOUNT);
                }
                break;
            case R.id.layout_setpaypwd:
                openWindow(WindowIDDefine.WINDOW_WALLET_SETPAYPWD);
                break;
            case R.id.layout_changepwd:
                openWindow(WindowIDDefine.WINDOW_WALLET_CHANGEPAYPWD);
                break;
            case R.id.layout_resetpaypwd:
                openWindow(WindowIDDefine.WINDOW_WALLET_RESETPAYPWD);
                break;
            case R.id.layout_statement:
                WebBean walletWebBean = new WebBean();
                walletWebBean.setRequest(true);
                walletWebBean.setTitle(getResources().getString(R.string.wallet_statement));
                walletWebBean.setName("walletStatement");
                Bundle bundle = new Bundle();
                bundle.putSerializable("webbean", walletWebBean);
                openWindow(WindowIDDefine.WINDOW_WEBVIEW, bundle);
                break;
        }
    }

    @Override
    public void responseWalletInfo(WalletInfoResponse response) {
        WalletInfoResponse.WalletResponseData responseData = response.getData();
        SharePresUtil.putFloat(SharePresUtil.KEY_BALANCE, responseData.getWallet());
        SharePresUtil.putInt(SharePresUtil.KEY_SETPAYACCOUNT, responseData.getIs_alipay());
        SharePresUtil.putInt(SharePresUtil.KEY_SETPAYPWD, responseData.getIs_paypassword());
        SharePresUtil.putString(SharePresUtil.KEY_ALIPAYNAME, responseData.getAlipay());
        tvMoney.setText(String.valueOf(responseData.getWallet()));
    }

    @Override
    public void onNotify(Message mes) {
        readyWallet();
    }
}
