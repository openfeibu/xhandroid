package cn.flyexp.mvc.mine;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import cn.flyexp.R;
import cn.flyexp.entity.ResetPayPwdRequest;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.util.CommonUtil;

/**
 * Created by txy on 2016/8/23 0023.
 */
public class ResetPayPwdWindow extends AbstractWindow implements View.OnClickListener, TextWatcher {

    private MineViewCallBack callBack;
    private EditText et_vertifycode;
    private TextView tv_vercode;
    private EditText et_pwd;
    private Button btn_reset;

    public ResetPayPwdWindow(MineViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_resetpaypwd);
        findViewById(R.id.iv_back).setOnClickListener(this);

        btn_reset = (Button) findViewById(R.id.btn_reset);
        btn_reset.setOnClickListener(this);

        tv_vercode = (TextView) findViewById(R.id.tv_vercode);
        tv_vercode.setOnClickListener(this);
        et_vertifycode = (EditText) findViewById(R.id.et_vertifycode);
        et_pwd = (EditText) findViewById(R.id.et_pwd);

        et_vertifycode.addTextChangedListener(this);
        et_pwd.addTextChangedListener(this);
    }

    private void countDown() {
        new CountDownTimer(60000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                tv_vercode.setClickable(false);
                tv_vercode.setText(millisUntilFinished / 1000 + "秒重新获取");
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
                String tokenVercode = getStringByPreference("token");
                if (tokenVercode.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                callBack.vercodeResetPayPwd(tokenVercode);
                countDown();
                break;
            case R.id.btn_reset:
                String pwd = et_pwd.getText().toString().trim();
                String vercode = et_vertifycode.getText().toString().trim();
                if (pwd.length() != 6) {
                    showToast("支付密码请输入完整");
                    return;
                }
                String token = getStringByPreference("token");
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                ResetPayPwdRequest resetPayPwdRequest = new ResetPayPwdRequest();
                resetPayPwdRequest.setToken(token);
                resetPayPwdRequest.setPay_password(CommonUtil.md5(pwd));
                resetPayPwdRequest.setSms_code(vercode);
                callBack.resetPayPwd(resetPayPwdRequest);
                v.setEnabled(false);
                showProgressDialog("正在重设...");
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
        String pwd = et_pwd.getText().toString().trim();
        String vercode = et_vertifycode.getText().toString().trim();
        if (pwd.equals("") || vercode.equals("")) {
            btn_reset.setEnabled(false);
            btn_reset.setAlpha(0.5f);
        } else {
            btn_reset.setEnabled(true);
            btn_reset.setAlpha(1f);
        }
    }
}
