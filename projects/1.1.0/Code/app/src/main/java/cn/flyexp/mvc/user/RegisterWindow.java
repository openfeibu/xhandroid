package cn.flyexp.mvc.user;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.flyexp.R;
import cn.flyexp.entity.RegisterRequest;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.util.CommonUtil;

/**
 * Created by txy on 2016/8/1 0001.
 */
public class RegisterWindow extends AbstractWindow implements View.OnClickListener, TextWatcher {

    private UserViewCallBack callBack;
    private EditText et_phone;
    private EditText et_pwd;
    private String mid;
    private Button btn_next;

    public RegisterWindow(UserViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();
        mid = WindowHelper.getStringByPreference("mid");
    }

    private void initView() {
        setContentView(R.layout.window_register);
        findViewById(R.id.iv_back).setOnClickListener(this);

        btn_next = (Button) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(this);

        et_phone = (EditText) findViewById(R.id.et_phone);
        et_pwd = (EditText) findViewById(R.id.et_pwd);

        et_phone.addTextChangedListener(this);
        et_pwd.addTextChangedListener(this);
        String lineNumber = WindowHelper.getStringByPreference("line_number");
        if (!lineNumber.equals("")) {
            et_phone.setText(lineNumber);
        }
    }

    @Override
    protected boolean canHandleKeyBackUp() {
        return !(et_phone.isFocused() || et_pwd.isFocused());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.btn_next:
                String phone = et_phone.getText().toString().trim();
                String pwd = et_pwd.getText().toString().trim();
                if (pwd.length() < 6 || pwd.length() > 16) {
                    WindowHelper.showToast("?????????????????????6~12???");
                    return;
                }
                Pattern pa = Pattern.compile("[a-zA-Z0-9.]{6,16}");
                Matcher ma = pa.matcher(pwd);
                if (!ma.matches()) {
                    WindowHelper.showToast("???????????????????????????");
                    return;
                }

                pa = Pattern.compile("1\\d{10}");
                ma = pa.matcher(phone);
                if (!ma.matches()) {
                    WindowHelper.showToast("??????????????????");
                    return;
                }
                RegisterRequest registerRequest = new RegisterRequest();
                registerRequest.setMobile_no(phone);
                registerRequest.setMid(mid);
                registerRequest.setPassword(CommonUtil.md5(pwd));
                callBack.registerInfoEnter(registerRequest);
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
        if (phone.equals("") || pwd.equals("")) {
            btn_next.setAlpha(0.5f);
            btn_next.setEnabled(false);
        } else {
            btn_next.setAlpha(1f);
            btn_next.setEnabled(true);
        }
    }
}
