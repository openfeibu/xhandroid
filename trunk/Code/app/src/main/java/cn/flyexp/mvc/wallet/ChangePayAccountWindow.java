package cn.flyexp.mvc.wallet;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.flyexp.R;
import cn.flyexp.entity.ChangeAliPayRequest;
import cn.flyexp.entity.ChangePayAccountVCRequest;
import cn.flyexp.entity.RegisterVerifyCodeRequest;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.mvc.mine.MineViewCallBack;

/**
 * Created by txy on 2016/8/23 0023.
 */
public class ChangePayAccountWindow extends AbstractWindow implements View.OnClickListener, TextWatcher {

    private WalletViewCallBack callBack;
    private EditText et_vertifycode;
    private EditText et_name;
    private EditText et_account;
    private TextView tv_vercode;
    private Button btn_change;

    public ChangePayAccountWindow(WalletViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_changepayaccount);
        findViewById(R.id.iv_back).setOnClickListener(this);

        btn_change = (Button) findViewById(R.id.btn_change);
        btn_change.setOnClickListener(this);

        tv_vercode = (TextView) findViewById(R.id.tv_vercode);
        tv_vercode.setOnClickListener(this);
        et_account = (EditText) findViewById(R.id.et_account);
        et_name = (EditText) findViewById(R.id.et_name);
        et_vertifycode = (EditText) findViewById(R.id.et_vertifycode);

        et_account.addTextChangedListener(this);
        et_name.addTextChangedListener(this);
        et_vertifycode.addTextChangedListener(this);
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
        btn_change.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.tv_vercode:
                String tokenVercode = WindowHelper.getStringByPreference("token");
                if (tokenVercode.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                callBack.vercodeChangePayAccount(tokenVercode);
                countDown();
                break;
            case R.id.btn_change:
                String account = et_account.getText().toString().trim();
                String name = et_name.getText().toString().trim();
                String vercode = et_vertifycode.getText().toString().trim();
                String token = WindowHelper.getStringByPreference("token");
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                ChangeAliPayRequest changeAliPayRequest = new ChangeAliPayRequest();
                changeAliPayRequest.setToken(token);
                changeAliPayRequest.setAlipay(account);
                changeAliPayRequest.setAlipay_name(name);
                changeAliPayRequest.setSms_code(vercode);
                callBack.changeAliPay(changeAliPayRequest);
                v.setEnabled(true);
                showProgressDialog("正在更换...");
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
        String account = et_account.getText().toString().trim();
        String name = et_name.getText().toString().trim();
        String vercode = et_vertifycode.getText().toString().trim();
        if (account.equals("") || name.equals("") || vercode.equals("")) {
            btn_change.setEnabled(false);
            btn_change.setAlpha(0.5f);
        } else {
            btn_change.setEnabled(true);
            btn_change.setAlpha(1f);
        }
    }
}
