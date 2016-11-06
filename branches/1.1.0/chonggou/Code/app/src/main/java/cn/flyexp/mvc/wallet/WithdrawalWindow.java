package cn.flyexp.mvc.wallet;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.flyexp.R;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.constants.Config;
import cn.flyexp.entity.WithdrawalRequest;
import cn.flyexp.framework.AbstractWindow;

/**
 * Created by txy on 2016/8/23 0023.
 */
public class WithdrawalWindow extends AbstractWindow implements View.OnClickListener, TextWatcher {

    private WalletViewCallBack callBack;
    private EditText et_fee;
    private WithdrawalRequest withdrawalRequst;
    private Button btn_withdrawal;
    private TextView tv_account;
    private TextView tv_money;

    public WithdrawalWindow(WalletViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_withdrawal);
        findViewById(R.id.iv_back).setOnClickListener(this);

        btn_withdrawal = (Button) findViewById(R.id.btn_withdrawal);
        btn_withdrawal.setOnClickListener(this);

        tv_account = (TextView) findViewById(R.id.tv_account);
        tv_money = (TextView) findViewById(R.id.tv_money);

        et_fee = (EditText) findViewById(R.id.et_fee);
        et_fee.addTextChangedListener(this);

        String account = WindowHelper.getStringByPreference("alipay");
        float balance = WindowHelper.getFloatByPreference("balance");
        if (account.equals("")) {
            return;
        }
        StringBuffer sbaccount = new StringBuffer(account);
        for (int i = 3; i < account.length() - 3; i++) {
            sbaccount.setCharAt(i, '*');
        }
        tv_account.setText("账户：" + sbaccount.toString());
        tv_money.setText(balance + "");
    }

    public void response() {
        dismissProgressDialog();
        btn_withdrawal.setEnabled(true);
    }

    public void payPwdResponse(String paypwd) {
        showProgressDialog("提现中...");
        withdrawalRequst.setPay_password(paypwd);
        callBack.withdrawalRequest(withdrawalRequst);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.btn_withdrawal:
                String feeStr = et_fee.getText().toString().trim();
                float fee = 0;
                fee = Float.parseFloat(feeStr);
                String token = WindowHelper.getStringByPreference("token");
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                withdrawalRequst = new WithdrawalRequest();
                withdrawalRequst.setToken(token);
                withdrawalRequst.setMoney(fee);
                callBack.verifiPayPwdEnter(NotifyIDDefine.RESULT_PAY_WITHDRAWAL);
                v.setEnabled(false);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String feeStr = et_fee.getText().toString().trim();
        float fee = 0;
        try {
            fee = Float.parseFloat(feeStr);
        } catch (NumberFormatException e) {
            btn_withdrawal.setEnabled(false);
            btn_withdrawal.setAlpha(0.5f);
        }
        if (feeStr.equals("") || fee < 10) {
            btn_withdrawal.setEnabled(false);
            btn_withdrawal.setAlpha(0.5f);
        } else {
            btn_withdrawal.setEnabled(true);
            btn_withdrawal.setAlpha(1f);
        }

    }
}
