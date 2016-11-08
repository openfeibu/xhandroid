package cn.flyexp.mvc.user;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.flyexp.R;
import cn.flyexp.entity.RegisterVerifyCodeRequest;
import cn.flyexp.entity.ResetPwdVerifyCodeRequest;
import cn.flyexp.entity.ResetPwdRequest;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.util.CommonUtil;

/**
 * Created by txy on 2016/8/1 0001.
 */
public class ResetPwdWindow extends AbstractWindow implements View.OnClickListener, TextWatcher {

    private UserViewCallBack callBack;
    private EditText et_phone;
    private EditText et_pwd;
    private EditText et_vertifycode;
    private TextView tv_vercode;
    private Button btn_reset;

    public ResetPwdWindow(UserViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_resetpwd);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_vercode).setOnClickListener(this);

        btn_reset = (Button) findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(this);

        tv_vercode = (TextView) findViewById(R.id.tv_vercode);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_vertifycode = (EditText) findViewById(R.id.et_vertifycode);

        et_phone.addTextChangedListener(this);
        et_pwd.addTextChangedListener(this);
        et_vertifycode.addTextChangedListener(this);

    }

    @Override
    protected boolean canHandleKeyBackUp() {
        return !(et_phone.isFocused() || et_pwd.isFocused() || et_vertifycode.isFocused());
    }

    private void countDown() {
        new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                tv_vercode.setClickable(false);
                tv_vercode.setText("重新获取" + millisUntilFinished / 1000 + "秒");
                tv_vercode.setTextColor(getResources().getColor(R.color.light_red));
            }

            @Override
            public void onFinish() {
                tv_vercode.setClickable(true);
                tv_vercode.setText("获取验证码");
                tv_vercode.setTextColor(getResources().getColor(R.color.light_blue));
            }
        }.start();
    }


    public void response() {
        dismissProgressDialog();
        btn_reset.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.tv_vercode:
                String phoneVerCode = et_phone.getText().toString().trim();
                if (phoneVerCode == null || phoneVerCode.equals("")) {
                    WindowHelper.showToast("请输入手机号码");
                    return;
                }
                ResetPwdVerifyCodeRequest verifyCodeRequest = new ResetPwdVerifyCodeRequest();
                verifyCodeRequest.setMobile_no(phoneVerCode);
                callBack.vercodeResetPwdRequest(verifyCodeRequest);
                countDown();
                break;
            case R.id.btn_reset:
                String phone = et_phone.getText().toString().trim();
                String pwd = et_pwd.getText().toString().trim();
                String vertifyCode = et_vertifycode.getText().toString().trim();
                if (pwd.length() < 6 || pwd.length() > 16) {
                    WindowHelper.showToast("密码长度必须为6~12位");
                    return;
                }
                Pattern pa = Pattern.compile("1\\d{10}");
                Matcher ma = pa.matcher(phone);
                if (!ma.matches()) {
                    WindowHelper.showToast("手机号码有误");
                    return;
                }
                ResetPwdRequest resetPwdRequest = new ResetPwdRequest();
                resetPwdRequest.setMobile_no(phone);
                resetPwdRequest.setPassword(CommonUtil.md5(pwd));
                resetPwdRequest.setSms_code(vertifyCode);
                callBack.resetPwdRequest(resetPwdRequest);
                v.setEnabled(false);
                showProgressDialog("找啊找...");
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
        String phone = et_phone.getText().toString().trim();
        String pwd = et_pwd.getText().toString().trim();
        String vercode = et_vertifycode.getText().toString().trim();
        if (phone.equals("") || pwd.equals("") || vercode.equals("")) {
            btn_reset.setAlpha(0.5f);
            btn_reset.setEnabled(false);
        } else {
            btn_reset.setAlpha(1f);
            btn_reset.setEnabled(true);
        }
    }
}
