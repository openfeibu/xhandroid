package cn.flyexp.window.wallet;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.flyexp.R;
import cn.flyexp.callback.wallet.ChangePayAccountCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ChangeAlipayRequest;
import cn.flyexp.entity.TokenRequest;
import cn.flyexp.framework.NotifyIDDefine;
import cn.flyexp.presenter.wallet.ChangePayAccountPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/10/30.
 */
public class ChangePayAccountWindow extends BaseWindow implements TextWatcher, ChangePayAccountCallback.ResponseCallback {

    @InjectView(R.id.edt_account)
    EditText edtAccount;
    @InjectView(R.id.edt_name)
    EditText edtName;
    @InjectView(R.id.edt_vercode)
    EditText edtVercode;
    @InjectView(R.id.tv_getvercode)
    TextView tvGetVercode;
    @InjectView(R.id.btn_change)
    Button btnChange;
    private ChangePayAccountPresenter changePayAccountPresenter;
    private SweetAlertDialog changePayAccountDialog;
    private String account;
    private String name;
    private String vercode;
    private CountDownTimer downTimer;
    private boolean isGettingCode;

    @Override
    protected int getLayoutId() {
        return R.layout.window_changepayaccount;
    }

    public ChangePayAccountWindow() {
        changePayAccountPresenter = new ChangePayAccountPresenter(this);
        edtAccount.addTextChangedListener(this);
        edtName.addTextChangedListener(this);
        edtVercode.addTextChangedListener(this);
        changePayAccountDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.commiting));
    }

    @OnClick({R.id.img_back, R.id.tv_getvercode, R.id.btn_change})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.tv_getvercode:
                readyGetVercode();
                break;
            case R.id.btn_change:
                readyChangePayAccount();
                break;
        }
    }

    private void readyGetVercode() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
            return;
        }
        isGettingCode = true;
        changePayAccountPresenter.requestVercode(new TokenRequest(token));
        countDown();
    }

    private void readyChangePayAccount() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
            return;
        }
        isGettingCode = false;
        ChangeAlipayRequest changeAlipayRequest = new ChangeAlipayRequest();
        changeAlipayRequest.setAlipay_name(name);
        changeAlipayRequest.setAlipay(account);
        changeAlipayRequest.setSms_code(vercode);
        changeAlipayRequest.setToken(token);
        changePayAccountPresenter.requestChangeAlipay(changeAlipayRequest);
        changePayAccountDialog.show();
    }

    private void countDown() {
        tvGetVercode.setEnabled(false);
        if (downTimer != null) {
            downTimer.cancel();
        }
        downTimer = new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                if (tvGetVercode == null) {
                    return;
                }
                tvGetVercode.setText(String.format(getResources().getString(R.string.format_vercode_renew_get), millisUntilFinished / 1000));
                tvGetVercode.setTextColor(getResources().getColor(R.color.light_red));
                tvGetVercode.setEnabled(false);
            }

            @Override
            public void onFinish() {
                if (tvGetVercode == null) {
                    return;
                }
                tvGetVercode.setText(getResources().getString(R.string.get_vercode));
                tvGetVercode.setTextColor(getResources().getColor(R.color.light_blue));
                tvGetVercode.setEnabled(true);
            }
        }.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (downTimer != null) {
            downTimer.cancel();
        }
        if (changePayAccountDialog != null) {
            changePayAccountDialog.dismiss();
        }
    }

    @Override
    public void requestFailure() {
        if(changePayAccountDialog.isShowing()){
            changePayAccountDialog.dismissWithAnimation();
        }
        if (downTimer != null) {
            downTimer.onFinish();
        }
    }

    @Override
    public void requestFinish() {
        if (isGettingCode) {
            return;
        }
        if(changePayAccountDialog.isShowing()){
            changePayAccountDialog.dismissWithAnimation();
        }
        if (downTimer != null) {
            downTimer.onFinish();
        }
    }

    @Override
    public void responseChangeAlipay(BaseResponse response) {
        showToast(R.string.change_success);
        if (downTimer != null) {
            downTimer.cancel();
        }
        getNotifyManager().notify(NotifyIDDefine.NOTICE_WALLET);
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
        account = edtAccount.getText().toString().trim();
        name = edtName.getText().toString().trim();
        vercode = edtVercode.getText().toString().trim();
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(vercode)) {
            btnChange.setEnabled(true);
            btnChange.setAlpha(1);
        } else {
            btnChange.setEnabled(false);
            btnChange.setAlpha(0.5f);
        }
    }


}
