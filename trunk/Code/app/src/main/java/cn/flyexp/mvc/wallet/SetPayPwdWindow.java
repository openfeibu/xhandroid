package cn.flyexp.mvc.wallet;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.flyexp.R;
import cn.flyexp.entity.SetPayPwdRequest;
import cn.flyexp.framework.AbstractWindow;
import cn.flyexp.framework.WindowHelper;
import cn.flyexp.mvc.mine.MineViewCallBack;
import cn.flyexp.util.CommonUtil;

/**
 * Created by txy on 2016/8/23 0023.
 */
public class SetPayPwdWindow extends AbstractWindow implements View.OnClickListener, TextWatcher {

    private WalletViewCallBack callBack;
    private EditText et_newPwd;
    private EditText et_newPwd2;
    private Button btn_set;

    public SetPayPwdWindow(WalletViewCallBack callBack) {
        super(callBack);
        this.callBack = callBack;
        initView();

    }

    private void initView() {
        setContentView(R.layout.window_setpaypwd);
        findViewById(R.id.iv_back).setOnClickListener(this);

        btn_set = (Button) findViewById(R.id.btn_set);
        btn_set.setOnClickListener(this);

        et_newPwd = (EditText) findViewById(R.id.et_newPwd);
        et_newPwd2 = (EditText) findViewById(R.id.et_newPwd2);

        et_newPwd.requestFocus();

        et_newPwd.addTextChangedListener(this);
        et_newPwd2.addTextChangedListener(this);

    }

    @Override
    protected boolean canHandleKeyBackUp() {
        return !(et_newPwd2.isFocused() || et_newPwd.isFocused());
    }

    public void response() {
        dismissProgressDialog();
        btn_set.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                hideWindow(true);
                break;
            case R.id.btn_set:
                String onePwd = et_newPwd.getText().toString().trim();
                String twoPwd = et_newPwd2.getText().toString().trim();
                if (onePwd.length() + twoPwd.length() != 12) {
                    WindowHelper.showToast("请输入完整");
                    return;
                }
                if (!onePwd.equals(twoPwd)) {
                    WindowHelper.showToast("两次密码输入不一致，请重新输入");
                    et_newPwd.setText("");
                    et_newPwd2.setText("");
                    return;
                }
                String token = WindowHelper.getStringByPreference("token");
                if (token.equals("")) {
                    callBack.loginWindowEnter();
                    return;
                }
                SetPayPwdRequest setPayPwdRequest = new SetPayPwdRequest();
                setPayPwdRequest.setToken(token);
                setPayPwdRequest.setPay_password(CommonUtil.md5(onePwd));
                callBack.setPayPwd(setPayPwdRequest);
                v.setEnabled(false);
                showProgressDialog("设置中...");
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
        String onePwd = et_newPwd.getText().toString().trim();
        String twoPwd = et_newPwd2.getText().toString().trim();
        if (onePwd.equals("") || twoPwd.equals("")) {
            btn_set.setEnabled(false);
            btn_set.setAlpha(0.5f);
        } else {
            btn_set.setEnabled(true);
            btn_set.setAlpha(1f);
        }
    }
}
