package cn.flyexp.window.wallet;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.wallet.WalletCallback;
import cn.flyexp.entity.AlipayInfoResponse;
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
public class WalletWindow extends BaseWindow implements NotifyManager.Notify, WalletCallback.ResponseCallback {

    @InjectView(R.id.tv_money)
    TextView tvMoney;
    @InjectView(R.id.tv_payinfo)
    TextView tvPayInfo;

    private WalletPresenter walletPresenter;
    private int setPayAccount;
    private int setPayPwd;
    private int auth;
    private PopupWindow picPopupWindow;
    private View paypwdLayout;

    @Override
    protected int getLayoutId() {
        return R.layout.window_wallet;
    }

    public WalletWindow() {
        walletPresenter = new WalletPresenter(this);
        getNotifyManager().register(NotifyIDDefine.NOTICE_WALLET, this);
        initView();
    }

    private void initView() {
        float balance = SharePresUtil.getFloat(SharePresUtil.KEY_BALANCE);
        String name = SharePresUtil.getString(SharePresUtil.KEY_ALIPAYNAME);
        setPayAccount = SharePresUtil.getInt(SharePresUtil.KEY_SETPAYACCOUNT);
        setPayPwd = SharePresUtil.getInt(SharePresUtil.KEY_SETPAYPWD);
        auth = SharePresUtil.getInt(SharePresUtil.KEY_AUTH);

        if (!TextUtils.isEmpty(name)) {
            setPayAccount(name);
        }
        paypwdLayout = LayoutInflater.from(getContext()).inflate(R.layout.pop_paypwd, null);
        paypwdLayout.findViewById(R.id.tv_change).setOnClickListener(popPayPwdOnListener);
        paypwdLayout.findViewById(R.id.tv_reset).setOnClickListener(popPayPwdOnListener);

        picPopupWindow = new PopupWindow(paypwdLayout,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        picPopupWindow.setFocusable(true);
        picPopupWindow.setTouchable(true);
        picPopupWindow.setOutsideTouchable(true);
        picPopupWindow.setBackgroundDrawable(new ColorDrawable());
        picPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
        picPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                changeWindowAlpha(1f);
            }
        });
        tvMoney.setText(String.format(getResources().getString(R.string.format_money), String.valueOf(balance)));
    }

    private OnClickListener popPayPwdOnListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_change:
                    openWindow(WindowIDDefine.WINDOW_WALLET_CHANGEPAYPWD);
                    break;
                case R.id.tv_reset:
                    openWindow(WindowIDDefine.WINDOW_WALLET_RESETPAYPWD);
                    break;
            }
            picPopupWindow.dismiss();
        }
    };

    private void readyWallet() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            openWindow(WindowIDDefine.WINDOW_LOGIN);
        } else {
            walletPresenter.requestWalletInfo(new TokenRequest(token));
        }
    }

    private void changeWindowAlpha(float v) {
        WindowManager.LayoutParams lp = ((Activity) getContext()).getWindow().getAttributes();
        lp.alpha = v;
        ((Activity) getContext()).getWindow().setAttributes(lp);
    }

    @OnClick({R.id.img_back, R.id.tv_statement, R.id.layout_detail, R.id.layout_withdrawal, R.id.layout_payinfo,
            R.id.layout_paypwd})
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
                    openWindow(WindowIDDefine.WINDOW_WALLET_CHANGEPAYACCOUNT);
                }
                break;
            case R.id.layout_paypwd:
                if (auth == 0) {
                    showToast(R.string.go_auth);
                    openWindow(WindowIDDefine.WINDOW_CERTIFITION);
                } else if (auth == 2) {
                    showToast(R.string.authing);
                } else if (auth == 1) {
                    if (setPayPwd == 0) {
                        openWindow(WindowIDDefine.WINDOW_WALLET_SETPAYPWD);
                    } else {
                        picPopupWindow.showAtLocation(this, Gravity.BOTTOM, 0, 0);
                        changeWindowAlpha(0.7f);
                    }
                }
                break;
            case R.id.tv_statement:
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
        tvMoney.setText(String.format(getResources().getString(R.string.format_money), String.valueOf(responseData.getWallet())));
    }

    @Override
    public void responseAlipayInfo(AlipayInfoResponse response) {
        AlipayInfoResponse.AlipayInfoResponseData responseData = response.getData();
        String account = responseData.getAlipay();
        if (account.equals("")) {
            return;
        }
        setPayAccount(account);
    }

    private void setPayAccount(String account) {
        StringBuffer sbaccount = new StringBuffer(account);
        for (int i = 3; i < account.length() - 3; i++) {
            sbaccount.setCharAt(i, '*');
        }
        tvPayInfo.setText(sbaccount.toString());
    }

    @Override
    public void onNotify(Message mes) {
        readyWallet();
    }
}
