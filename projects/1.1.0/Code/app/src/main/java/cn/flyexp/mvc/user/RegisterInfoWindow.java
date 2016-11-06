package cn.flyexp.mvc.user;

import android.nfc.FormatException;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import cn.flyexp.R;
import cn.flyexp.entity.RegisterRequest;
import cn.flyexp.entity.RegisterVerifyCodeRequest;
import cn.flyexp.entity.WebBean;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.util.LogUtil;

/**
 * Created by txy on 2016/8/2 0002.
 */
public class RegisterInfoWindow extends AbstractWindow implements View.OnClickListener, TextWatcher {

    private UserViewCallBack callBack;
    private TextView tv_boy;
    private TextView tv_girl;
    private EditText et_nickname;
    private EditText et_year;
    private RegisterRequest registerRequest;
    private TextView tv_phone;
    private EditText et_vertifycode;
    private TextView tv_vercode;
    private int gender = 1;
    private Button btn_register;

    public RegisterInfoWindow(UserViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_register_info);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_agreement).setOnClickListener(this);

        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);

        tv_boy = (TextView) findViewById(R.id.tv_boy);
        tv_girl = (TextView) findViewById(R.id.tv_girl);
        tv_boy.setOnClickListener(this);
        tv_girl.setOnClickListener(this);

        et_nickname = (EditText) findViewById(R.id.et_nickname);
        et_year = (EditText) findViewById(R.id.et_year);
        et_vertifycode = (EditText) findViewById(R.id.et_vertifycode);

        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_vercode = (TextView) findViewById(R.id.tv_vercode);
        tv_vercode.setOnClickListener(this);

        et_nickname.addTextChangedListener(this);
        et_year.addTextChangedListener(this);
        et_vertifycode.addTextChangedListener(this);

    }

    @Override
    protected boolean canHandleKeyBackUp() {
        return false;
    }

    public void initRequest(RegisterRequest registerRequest) {
        this.registerRequest = registerRequest;
        tv_phone.setText("你填写的手机为" + registerRequest.getMobile_no());
    }

    public RegisterRequest getRegisterRequest() {
        return registerRequest;
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
        btn_register.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.tv_boy:
                gender = 1;
                tv_boy.setTextColor(getResources().getColor(R.color.light_blue));
                tv_girl.setTextColor(getResources().getColor(R.color.app_background));
                break;
            case R.id.tv_girl:
                gender = 2;
                tv_boy.setTextColor(getResources().getColor(R.color.app_background));
                tv_girl.setTextColor(getResources().getColor(R.color.light_red));
                break;
            case R.id.tv_vercode:
                RegisterVerifyCodeRequest verifyCodeRequest = new RegisterVerifyCodeRequest();
                verifyCodeRequest.setMobile_no(registerRequest.getMobile_no());
                callBack.vercodeRegisterRequest(verifyCodeRequest);
                countDown();
                break;
            case R.id.btn_register:
                String nickName = et_nickname.getText().toString().trim();
                String yearStr = et_year.getText().toString().trim();
                String vertifyCode = et_vertifycode.getText().toString().trim();
                int inYear = 0;
                try {
                    inYear = Integer.parseInt(yearStr);
                } catch (NumberFormatException e) {
                    WindowHelper.showToast("入学年份输入非法");
                    return;
                }
                int year = Calendar.getInstance().get(Calendar.YEAR);
                if (inYear < 2000 || inYear > year) {
                    WindowHelper.showToast("入学年份输入不符");
                    return;
                }
                registerRequest.setNickname(nickName);
                registerRequest.setEnrollment_year(inYear);
                registerRequest.setGender(gender);
                registerRequest.setSms_code(vertifyCode);
                callBack.registerRequest(registerRequest);
                showProgressDialog("正在注册...");
                v.setEnabled(false);
                break;
            case R.id.tv_agreement:
                WebBean agreeWebBean = new WebBean();
                agreeWebBean.setRequest(true);
                agreeWebBean.setTitle("用户服务协议");
                agreeWebBean.setName("userAgreement");
                callBack.webWindowEnter(agreeWebBean);
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
        String nickname = et_nickname.getText().toString().trim();
        String year = et_year.getText().toString().trim();
        String vercode = et_vertifycode.getText().toString().trim();
        if (nickname.equals("") || year.equals("") || vercode.equals("")) {
            btn_register.setAlpha(0.5f);
            btn_register.setEnabled(false);
        } else {
            btn_register.setAlpha(1f);
            btn_register.setEnabled(true);
        }
    }
}
