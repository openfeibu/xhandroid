package cn.flyexp.window.wallet;

import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.wallet.WithdrawalCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.WithdrawalRequest;
import cn.flyexp.presenter.wallet.WithdrawalPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.EncodeUtil;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.PasswordView;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/10/30.
 */
public class WithdrawalWindow extends BaseWindow implements TextWatcher, WithdrawalCallback.ResponseCallback {

    @InjectView(R.id.tv_money)
    TextView tvMoney;
    @InjectView(R.id.tv_account)
    TextView tvAccount;
    @InjectView(R.id.edt_money)
    EditText edtMoney;
    @InjectView(R.id.btn_withdrawal)
    Button btnWithdrawal;
    private WithdrawalPresenter withdrawalPresenter;
    private SweetAlertDialog withdrawalDialog;
    private float money;
    private AlertDialog inputPayPwdDialog;
    private final View inputPayPwdLayout;
    private String paypwd;

    @Override
    protected int getLayoutId() {
        return R.layout.window_withdrawal;
    }

    public WithdrawalWindow() {
        withdrawalPresenter = new WithdrawalPresenter(this);
        edtMoney.addTextChangedListener(this);
        withdrawalDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.commiting));
        inputPayPwdLayout = LayoutInflater.from(getContext()).inflate(R.layout.dialog_input_paypwd, null);
        String account = SharePresUtil.getString(SharePresUtil.KEY_ALIPAYNAME);
        float balance = SharePresUtil.getFloat(SharePresUtil.KEY_BALANCE);
        if (TextUtils.isEmpty(account)) {
            return;
        }
        StringBuffer sbaccount = new StringBuffer(account);
        for (int i = 3; i < account.length() - 3; i++) {
            sbaccount.setCharAt(i, '*');
        }
        tvAccount.setText(String.format(getResources().getString(R.string.hint_withdrawal), sbaccount.toString()));
        tvMoney.setText(String.valueOf(balance));
    }

    @OnClick({R.id.img_back, R.id.btn_withdrawal})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.btn_withdrawal:
                showInputPayPwd();
                break;
        }
    }

    private void showInputPayPwd() {
        if (inputPayPwdDialog == null) {
            inputPayPwdDialog = new AlertDialog.Builder(getContext()).setView(inputPayPwdLayout).create();
        }
        inputPayPwdDialog.show();
        final PasswordView passwordView = (PasswordView) inputPayPwdDialog.findViewById(R.id.edt_paypwd);
        passwordView.requestFocus();
        passwordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                paypwd = passwordView.getText().toString();
                if (paypwd.length() == 6) {
                    inputPayPwdDialog.dismiss();
                    readyWithdrawal();
                }
            }
        });
    }

    private void readyWithdrawal() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
            return;
        }
        WithdrawalRequest withdrawalRequest = new WithdrawalRequest();
        withdrawalRequest.setMoney(money);
        withdrawalRequest.setToken(token);
        withdrawalRequest.setPay_password(EncodeUtil.md5Encode(paypwd));
        withdrawalPresenter.requestWithdrawal(withdrawalRequest);
        withdrawalDialog.show();
    }

    @Override
    public void requestFailure() {
        if(withdrawalDialog.isShowing()){
            withdrawalDialog.dismissWithAnimation();
        }
    }

    @Override
    public void requestFinish() {
        if(withdrawalDialog.isShowing()){
            withdrawalDialog.dismissWithAnimation();
        }
    }

    @Override
    public void responseWithdrawal(BaseResponse response) {
        showToast(R.string.withdrawal_success);
        hideWindow(true);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String moneyStr = edtMoney.getText().toString().trim();
        try {
            money = Float.parseFloat(moneyStr);
        } catch (NumberFormatException e) {
            btnWithdrawal.setEnabled(false);
            btnWithdrawal.setAlpha(0.5f);
        }
        if (TextUtils.isEmpty(moneyStr) || money < 10) {
            btnWithdrawal.setEnabled(false);
            btnWithdrawal.setAlpha(0.5f);
        } else {
            btnWithdrawal.setEnabled(true);
            btnWithdrawal.setAlpha(1f);
        }

    }
}
