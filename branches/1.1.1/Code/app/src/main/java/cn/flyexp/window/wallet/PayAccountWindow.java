package cn.flyexp.window.wallet;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.wallet.PayAccountCallback;
import cn.flyexp.entity.AlipayInfoResponse;
import cn.flyexp.entity.TokenRequest;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.presenter.wallet.PayAccountPresenter;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/10/30.
 */
public class PayAccountWindow extends BaseWindow implements PayAccountCallback.ResponseCallback {

    @InjectView(R.id.tv_account)
    TextView tvAccount;
    @InjectView(R.id.tv_name)
    TextView tvName;
    private final PayAccountPresenter payAccountPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.window_payaccount;
    }

    public PayAccountWindow() {
        payAccountPresenter = new PayAccountPresenter(this);
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
            return;
        }
        payAccountPresenter.requestAlipayInfo(new TokenRequest(token));
    }

    @OnClick({R.id.img_back, R.id.btn_change})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.btn_change:
                openWindow(WindowIDDefine.WINDOW_WALLET_CHANGEPAYACCOUNT);
                break;
        }
    }

    @Override
    public void responseAlipayInfo(AlipayInfoResponse response) {
        AlipayInfoResponse.AlipayInfoResponseData responseData = response.getData();
        String account = responseData.getAlipay();
        String name = responseData.getAlipay_name();
        if (account.equals("") || name.equals("")) {
            return;
        }
        StringBuffer sbaccount = new StringBuffer(account);
        for (int i = 3; i < account.length() - 3; i++) {
            sbaccount.setCharAt(i, '*');
        }
        StringBuffer sbname = new StringBuffer(name);
        sbname.setCharAt(1, '*');
        tvAccount.setText(String.format(getResources().getString(R.string.payaccount), sbaccount.toString()));
        tvName.setText(String.format(getResources().getString(R.string.hint_alipayname_format), sbname.toString()));
    }
}
