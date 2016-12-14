package cn.flyexp.window.user;

import android.os.Bundle;
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
import cn.flyexp.entity.ResetPwdRequest;
import cn.flyexp.framework.WindowIDDefine;
import cn.flyexp.util.EncodeUtil;
import cn.flyexp.util.PatternUtil;
import cn.flyexp.window.BaseWindow;

/**
 * Created by tanxinye on 2016/10/29.
 */
public class ResetPwdWindow extends BaseWindow implements TextWatcher {

    @InjectView(R.id.edt_phone)
    EditText edtPhone;
    @InjectView(R.id.edt_password)
    EditText edtPassword;
    @InjectView(R.id.btn_next)
    Button btnNext;

    private String phone;
    private String pwd;

    @Override
    protected int getLayoutId() {
        return R.layout.window_resetpwd;
    }

    public ResetPwdWindow() {
        edtPhone.addTextChangedListener(this);
        edtPassword.addTextChangedListener(this);
    }

    @OnClick({R.id.img_back, R.id.btn_next})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                hideWindow(true);
                break;
            case R.id.btn_next:
                readyResetPwdRequest();
                break;
        }
    }

    private void readyResetPwdRequest() {
        if (checkPhoneAndPwd()) {
            return;
        }
        ResetPwdRequest resetPwdRequest = new ResetPwdRequest();
        resetPwdRequest.setMobile_no(phone);
        resetPwdRequest.setPassword(EncodeUtil.md5Encode(pwd));
        Bundle bundle = new Bundle();
        bundle.putSerializable("resetPwdRequest", resetPwdRequest);
        openWindow(WindowIDDefine.WINDOW_RESETPWD_VERCODE, bundle);
    }

    private boolean checkPhoneAndPwd() {
        if (!PatternUtil.validatePhone(phone)) {
            showToast(R.string.phone_format_illegal);
            return true;
        }
        if (pwd.length() > 16 || pwd.length() < 6) {
            showToast(R.string.pwd_length_illegal);
            return true;
        }
        if (!PatternUtil.validateUserPwd(pwd)) {
            showToast(R.string.pwd_format_illegal);
            return true;
        }
        return false;
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
        pwd = edtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(pwd)) {
            btnNext.setEnabled(false);
        } else {
            btnNext.setEnabled(true);
        }
    }
}
