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
import cn.flyexp.callback.wallet.ResetPayPwdCallback;
import cn.flyexp.entity.BaseResponse;
import cn.flyexp.entity.ResetPayPwdRequest;
import cn.flyexp.entity.TokenRequest;
import cn.flyexp.presenter.wallet.ResetPayPwdPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.EncodeUtil;
import cn.flyexp.util.SharePresUtil;
import cn.flyexp.view.PasswordView;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/10/30.
 */
public class ResetPayPwdWindow extends BaseWindow implements TextWatcher, ResetPayPwdCallback.ResponseCallback {

    @InjectView(R.id.edt_pwd)
    PasswordView edtPwd;
    @InjectView(R.id.edt_vercode)
    EditText edtVercode;
    @InjectView(R.id.tv_getvercode)
    TextView tvGetVercode;
    @InjectView(R.id.btn_reset)
    Button btnReset;
    private ResetPayPwdPresenter resetPayPwdPresenter;
    private SweetAlertDialog resetPayPwdDialog;
    private String pwd;
    private String vercode;
    private CountDownTimer downTimer;
    private boolean isGettingCode;

    @Override
    protected int getLayoutId() {
        return R.layout.window_resetpaypwd;
    }

    public ResetPayPwdWindow() {
        resetPayPwdPresenter = new ResetPayPwdPresenter(this);
        resetPayPwdDialog = DialogHelper.getProgressDialog(getContext(), getResources().getString(R.string.commiting));

        edtPwd.addTextChangedListener(this);
        edtVercode.addTextChangedListener(this);
    }

    @OnClick({R.id.img_back, R.id.tv_getvercode, R.id.btn_reset})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.tv_getvercode:
                readyGetVercode();
                break;
            case R.id.btn_reset:
                readyResetPayPwd();
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
        resetPayPwdPresenter.requestVerCode(new TokenRequest(token));
        countDown();
    }

    private void readyResetPayPwd() {
        String token = SharePresUtil.getString(SharePresUtil.KEY_TOKEN);
        if (TextUtils.isEmpty(token)) {
            renewLogin();
            return;
        }
        isGettingCode = false;
        ResetPayPwdRequest resetPayPwdRequest = new ResetPayPwdRequest();
        resetPayPwdRequest.setToken(token);
        resetPayPwdRequest.setSms_code(vercode);
        resetPayPwdRequest.setPay_password(EncodeUtil.md5Encode(pwd));
        resetPayPwdPresenter.requestResetPayPwd(resetPayPwdRequest);
        resetPayPwdDialog.show();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (downTimer != null) {
            downTimer.cancel();
        }
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
    public void requestFailure() {
        if (resetPayPwdDialog.isShowing()) {
            resetPayPwdDialog.dismissWithAnimation();
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
        if (resetPayPwdDialog.isShowing()) {
            resetPayPwdDialog.dismissWithAnimation();
        }

        if (downTimer != null) {
            downTimer.onFinish();
        }
    }

    @Override
    public void responseResetPayPwd(BaseResponse response) {
        if (downTimer != null) {
            downTimer.cancel();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        pwd = edtPwd.getText().toString().trim();
        vercode = edtVercode.getText().toString().trim();
        if (pwd.length() != 6 || TextUtils.isEmpty(vercode)) {
            btnReset.setAlpha(0.5f);
            btnReset.setEnabled(false);
        } else {
            btnReset.setAlpha(1);
            btnReset.setEnabled(true);
        }
    }
}
