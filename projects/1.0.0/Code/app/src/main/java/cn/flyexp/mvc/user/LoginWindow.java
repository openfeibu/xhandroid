package cn.flyexp.mvc.user;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.flyexp.R;
import cn.flyexp.entity.ImageVerifyResponse;
import cn.flyexp.entity.LoginRequest;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.util.CommonUtil;
import cn.flyexp.util.LogUtil;

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
        et_vertifycode = (EditText) findViewById(R.id.et_vertifycode);
        iv_vertifycode = (ImageView) findViewById(R.id.iv_vertifycode);
        vertifyCodeLayout = findViewById(R.id.vertifyCodeLayout);

        et_phone.addTextChangedListener(this);
        et_pwd.addTextChangedListener(this);
        et_vertifycode.addTextChangedListener(this);

        String lineNumber = getStringByPreference("line_number");
        if (!lineNumber.equals("")) {
            et_phone.setText(lineNumber);
        }
    }

    public void vertifyLogin() {
        if (isVertify) {
            et_vertifycode.setText("");
            showToast("?????????????????????");
        } else {
            showToast("?????????????????????????????????");
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
                String phone = et_phone.getText().toString().trim();
                String pwd = et_pwd.getText().toString().trim();
                String vertifyCode = et_vertifycode.getText().toString().trim();
                if (!isVertify) {
                    vertifyCode = "0";
                }
                if (pwd.length() < 6 || pwd.length() > 16) {
                    showToast("?????????????????????6~12???");
                    return;
                }
                Pattern pa = Pattern.compile("[a-zA-Z0-9]{6,16}");
                Matcher ma = pa.matcher(pwd);
                if (!ma.matches()) {
                    showToast("???????????????????????????");
                    return;
                }
                pa = Pattern.compile("1\\d{10}");
                ma = pa.matcher(phone);
                if (!ma.matches()) {
                    showToast("??????????????????");
                    return;
                }
                String mid = getStringByPreference("mid");
                String device_token = getStringByPreference("device_token");
                LoginRequest loginRequest = new LoginRequest();
                loginRequest.setMobile_no(phone);
                loginRequest.setPassword(CommonUtil.md5(pwd));
                loginRequest.setVerify_code(vertifyCode);
                loginRequest.setMid(mid);
                loginRequest.setPlatform("and");
                loginRequest.setDevice_token(device_token.equals("") ? "0" : device_token);
                callBack.loginRequest(loginRequest);
                v.setEnabled(false);
                showProgressDialog("????????????...");
                break;
            case R.id.tv_register:
                callBack.registerEnter();
                break;
            case R.id.tv_findpwd:
                callBack.resetPwdEnter();
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
        if (phone.equals("") || pwd.equals("") || (isVertify && vercode.equals(""))) {
            btn_login.setAlpha(0.5f);
            btn_login.setEnabled(false);
        } else {
            btn_login.setAlpha(1f);
            btn_login.setEnabled(true);
        }
    }
}
