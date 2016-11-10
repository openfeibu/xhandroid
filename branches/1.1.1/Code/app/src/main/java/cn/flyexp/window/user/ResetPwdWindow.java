package cn.flyexp.window.user;

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
import cn.flyexp.callback.user.ResetPwdCallback;
import cn.flyexp.entity.ResetPwdRequest;
import cn.flyexp.entity.ResetPwdResponse;
import cn.flyexp.entity.SmsCodeRequest;
import cn.flyexp.presenter.user.ResetPwdPresenter;
import cn.flyexp.util.DialogHelper;
import cn.flyexp.util.PatternUtil;
import cn.flyexp.window.BaseWindow;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by tanxinye on 2016/10/29.
 */
public class ResetPwdWindow extends BaseWindow implements TextWatcher, ResetPwdCallback.ResponseCallback {

    @InjectView(R.id.edt_phone)
    EditText edtPhone;
    @InjectView(R.id.edt_pwd)
    EditText edtPwd;
    @InjectView(R.id.edt_vercode)
    EditText edtVercode;
    @InjectView(R.id.tv_getvercode)
    TextView tvGetVercode;
    @InjectView(R.id.btn_reset)
    Button btnReset;

    private ResetPwdPresenter resetPwdPresenter;
    private String phone;
    private String pwd;
    private String vercode;
    private final SweetAlertDialog resetPwdDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.window_resetpwd;
    }

    public ResetPwdWindow() {
        resetPwdPresenter = new ResetPwdPresenter(this);
        edtPhone.addTextChangedListener(this);
        edtPwd.addTextChangedListener(this);
        edtVercode.addTextChangedListener(this);
        resetPwdDialog = DialogHelper.getProgressDialog(getContext(),getResources().getString(R.string.reseting));
    }

    @OnClick({R.id.img_back,R.id.tv_getvercode, R.id.btn_reset})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.tv_getvercode:
                resetPwdPresenter.requestVercode(new SmsCodeRequest(phone));
                countDown();
                break;
            case R.id.btn_reset:
                readyResetPwdRequest();
                resetPwdDialog.show();
                break;
        }
    }

    private void countDown() {
        new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                tvGetVercode.setText(String.format(getResources().getString(R.string.vercode_renew_get), millisUntilFinished / 1000));
                tvGetVercode.setTextColor(getResources().getColor(R.color.light_red));
                tvGetVercode.setEnabled(false);
            }

            @Override
            public void onFinish() {
                tvGetVercode.setText(getResources().getString(R.string.get_vercode));
                tvGetVercode.setTextColor(getResources().getColor(R.color.light_blue));
                tvGetVercode.setEnabled(true);
            }
        }.start();
    }

    private void readyResetPwdRequest() {
        if (pwd.length() < 6 || pwd.length() > 16) {
            showToast(getResources().getString(R.string.pwd_length_illegal));
            return;
        }
        ResetPwdRequest resetPwdRequest = new ResetPwdRequest();
        resetPwdRequest.setMobile_no(phone);
        resetPwdRequest.setPassword(pwd);
        resetPwdRequest.setSms_code(vercode);
        resetPwdPresenter.requestResetPwd(resetPwdRequest);
    }

    @Override
    public void requestFailure() {
        resetPwdDialog.dismissWithAnimation();
    }

    @Override
    public void responseResetPwd(ResetPwdResponse response) {
        resetPwdDialog.dismissWithAnimation();
        showToast(R.string.reset_success);
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
        phone = edtPhone.getText().toString().trim();
        pwd = edtPwd.getText().toString().trim();
        vercode = edtVercode.getText().toString().trim();
        if (!TextUtils.isEmpty(phone) && PatternUtil.validatePhone(phone)) {
            tvGetVercode.setEnabled(true);
            tvGetVercode.setAlpha(1);
        } else if (!TextUtils.isEmpty(phone) && PatternUtil.validatePhone(phone) && !TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(vercode)) {
            tvGetVercode.setEnabled(true);
            tvGetVercode.setAlpha(1);
            btnReset.setEnabled(true);
            btnReset.setAlpha(1);
        } else {
            tvGetVercode.setEnabled(false);
            tvGetVercode.setAlpha(0.5f);
            btnReset.setEnabled(false);
            btnReset.setAlpha(0.5f);
        }
    }
}
