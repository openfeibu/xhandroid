package cn.flyexp.mvc.user;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.flyexp.R;
import cn.flyexp.constants.SharedPrefs;
import cn.flyexp.entity.ImageVerifyResponse;
import cn.flyexp.entity.LoginRequest;
import cn.flyexp.framework.AbstractController;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.util.CommonUtil;

/**
 * Created by txy on 2016/7/31 0031.
 */
public class LoginWindow extends AbstractWindow implements View.OnClickListener, TextWatcher {

    private UserViewCallBack callBack;
    private EditText et_phone;
    private EditText et_pwd;
    private EditText et_vertifycode;
    private ImageView iv_vertifycode;
    private View vertifyCodeLayout;
    private boolean isVertify = false;
    private Button btn_login;

    public LoginWindow(UserViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
    }

    private void initView() {
        setContentView(R.layout.window_login);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_register).setOnClickListener(this);
        findViewById(R.id.tv_findpwd).setOnClickListener(this);
        findViewById(R.id.iv_vertifycode).setOnClickListener(this);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);

        et_phone = (EditText) findViewById(R.id.et_phone);
        et_pwd = (EditText) findViewById(R.id.et_pwd);
        et_pwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    login();
                }
                return false;
            }
        });
        et_vertifycode = (EditText) findViewById(R.id.et_vertifycode);
        iv_vertifycode = (ImageView) findViewById(R.id.iv_vertifycode);
        vertifyCodeLayout = findViewById(R.id.vertifyCodeLayout);

        et_phone.addTextChangedListener(this);
        et_pwd.addTextChangedListener(this);
        et_vertifycode.addTextChangedListener(this);

        String lineNumber = WindowHelper.getStringByPreference("line_number");
        if (!lineNumber.equals("")) {
            et_phone.setText(lineNumber);
        }
        if("".equals(et_phone.getText().toString())) {
            et_phone.setText(callBack.getUIData(WindowCallBack.UIDataKeysDef.LOGIN_PHONE_NUM));
        }
    }

    public void vertifyLogin() {
        if (isVertify) {
            et_vertifycode.setText("");
            WindowHelper.showToast("验证码输入错误");
        } else {
            WindowHelper.showToast("登录异常，请输入验证码");
            isVertify = true;
            vertifyCodeLayout.setVisibility(View.VISIBLE);
            btn_login.setAlpha(0.5f);
            btn_login.setEnabled(false);
        }
        callBack.imageVerifyRequest();
    }

    public void imageVerifyResponse(ImageVerifyResponse imageVerifyResponse) {
        Picasso.with(getContext()).load(imageVerifyResponse.getUrl()).into(iv_vertifycode);
    }


    public void response() {
        dismissProgressDialog();
        iv_vertifycode.setEnabled(true);
        btn_login.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.iv_vertifycode:
                callBack.imageVerifyRequest();
                v.setEnabled(false);
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.tv_register:
                callBack.registerEnter();
                break;
            case R.id.tv_findpwd:
                callBack.resetPwdEnter();
                break;
        }
    }


    private void login(){
        String phone = et_phone.getText().toString().trim();
        String pwd = et_pwd.getText().toString().trim();
        String vertifyCode = et_vertifycode.getText().toString().trim();
        if (!isVertify) {
            vertifyCode = "0";
        }
        if (pwd.length() < 6 || pwd.length() > 16) {
            WindowHelper.showToast("密码长度必须为6~12位");
            return;
        }
        Pattern pa = Pattern.compile("[a-zA-Z0-9]{6,16}");
        Matcher ma = pa.matcher(pwd);
        if (!ma.matches()) {
            WindowHelper.showToast("密码不支持特殊字符");
            return;
        }
        pa = Pattern.compile("1\\d{10}");
        ma = pa.matcher(phone);
        if (!ma.matches()) {
            WindowHelper.showToast("手机号码有误");
            return;
        }

        String push = WindowHelper.getStringByPreference(SharedPrefs.KET_PUSH);
        String mid = WindowHelper.getStringByPreference("mid");
        String device_token = WindowHelper.getStringByPreference("device_token");
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setMobile_no(phone);
        loginRequest.setPassword(CommonUtil.md5(pwd));
        loginRequest.setVerify_code(vertifyCode);
        loginRequest.setMid(mid);
        loginRequest.setPlatform("and");
        loginRequest.setDevice_token(device_token.equals("") ? "0" : device_token);
        if(push.equals(SharedPrefs.VALUE_XMPUSH)){
            loginRequest.setPush_server("xiaomi");
        }else if(push.equals(SharedPrefs.VALUE_XGPUSH)){
            loginRequest.setPush_server("xinge");
        }
        callBack.loginRequest(loginRequest);
        btn_login.setEnabled(false);
        showProgressDialog("正在登录...");
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
        callBack.setUIData(WindowCallBack.UIDataKeysDef.LOGIN_PHONE_NUM, phone);
        String pwd = et_pwd.getText().toString().trim();
        String vercode = et_vertifycode.getText().toString().trim();
        if (phone.equals("") || pwd.equals("") || (isVertify && vercode.equals(""))) {
            btn_login.setAlpha(0.5f);
            btn_login.setEnabled(false);
        } else {
            btn_login.setAlpha(1f);
            btn_login.setEnabled(true);
        }
    }
}
